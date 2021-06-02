import AXIOS from "@/http_client";
import {setLoading} from "@/store/store";
import axios from "axios";


export function restService(entityUri, params) {
    const addparams = params ? params : {};

    function get(id) {
        setLoading(true);
        return getByUrl(entityUri + "/" + id, {params: addparams})
    }

    async function loadLinks(data) {
        for (const link in data._links) {
            if (link === "item" || link === "self") {
                continue
            }
            await axios.get(data._links[link].href).then((resp) => {
                if (resp.data._embedded) {
                    data[link] = resp.data._embedded.items.map((item) => item._links.item.href)
                } else {
                    data[link] = resp.data._links.item.href;
                }
            }).catch(() => {
            })
        }
    }

    function getByUrl(url) {
        setLoading(true);
        return AXIOS.get(url).then(async (response) => {
            const data = response.data
            await loadLinks(data)
            return data
        }).finally(() => setLoading())
    }

    async function put(entity) {
        setLoading(true);
        return await axios.put(entity._links.self.href, entity).then(async (resp) => {
            await putLinks(entity)
            const data = resp.data
            await loadLinks(data)
            return data
        }).finally(() => setLoading())
    }

    async function putLinks(entity) {
        const associations = {};
        for (const propName in entity._links) {
            if (entity[propName]) {
                const target = entity._links[propName].href;
                if (Array.isArray(entity[propName]))
                    associations[target] = entity[propName]
                else
                    associations[target] = [entity[propName]]
            }
        }
        for (const href in associations) {
            await axios.put(href, associations[href].join('\n'), {headers: {'Content-Type': 'text/uri-list'}})
        }
    }

    async function create(entity) {
        setLoading(true)
        return await AXIOS.post(entityUri, entity).then(async resp => {
            const data = resp.data;
            await loadLinks(data)
            return data
        }).finally(() => setLoading())
    }

    async function save(entity) {
        let action;
        if (entity.new)
            action = create(entity)
        else
            action = put(entity)
        setLoading(true)
        const data = await action.then((data) => {
            return data;
        }).finally(() => setLoading())
        return data;
    }

    function remove(entity) {
        return AXIOS.delete(entityUri + '/' + entity.id, {params: addparams}).then(response => {
            return response.data
        }).finally(() => setLoading())
    }


    function page(options) {
        const sorts = options.sortBy.map(function (srt, index) {
            let result = srt;
            if (options.sortDesc[index])
                result = result + ",desc";
            return result;
        });
        const queryParams = {
            page: options.page - 1,
            size: options.itemsPerPage,
            sort: sorts,
            ...addparams
        };
        setLoading(true)
        return AXIOS.get(entityUri, {
            params: queryParams
        }).then(response => {
            return response.data
        }).finally(() => setLoading())
    }

    return {get, save, page, del: remove};
}

export function restAllServices() {
    AXIOS.get("").then(response => {
        const services = {};
        response.data._links.items.forEach((href => {
            const item = lastItem(href)
            const rest = restService(item);
            services[item] = rest;
        }))
        return services;
    })
}

function lastItem(path) {
    return path.replace(/.*\/(\w).*/g, "$1")
}
