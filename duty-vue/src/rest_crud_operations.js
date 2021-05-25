import axios from "@/http_client";
import {error, info, setLoading} from "@/store/store";
import I18N from "@/i18n";


export function restService(entityUri, params) {
    const i18n = I18N;
    const addparams = params ? params : {};


    function toLink(obj) {
        if (obj !== null && typeof obj === "object")
            if (obj.href) {
                return obj.href
            } else {
                for (const key in obj) {
                    const href = toLink(obj[key])
                    if (href)
                        return href
                }
            }
        return obj
    }

    function extractLink(obj) {
        return toLink(obj);
    }


    function copyWithLinks(obj) {
        const data = {};
        for (const key of Object.keys(obj).filter((el) => "_links" !== el)) {
            const val = extractLink(obj[key]);
            if (val !== null)
                data[key] = val
        }
        return data;
    }


    function get(id) {
        setLoading(true);
        return axios.get(entityUri + "/" + id, {params: addparams}).then((response) => {
            return response.data
        })
            .catch(e => error(e))
            .finally(() => setLoading())
    }

    function edit(entity) {
        const data = copyWithLinks(entity)
        setLoading(true);
        return axios.put(entityUri + "/" + entity.id, data, {params: addparams}).then(response => {
            return response.data
        }).catch(e => error(e)).finally(() => setLoading())
    }

    function create(entity) {
        const data = copyWithLinks(entity)
        setLoading(true)
        return axios.post(entityUri, data, {params: addparams}).then(response => {
            return response.data
        }).catch(e => error(e)).finally(() => setLoading())
    }

    function save(entity) {
        let result;
        info(entity)
        if (entity.new)
            result = create(entity)
        else
            result = edit(entity)

        result.then(() => (data) => {
            const msg = i18n.t("crud.saved.success");
            info(msg)
            return data;
        }).finally(() => setLoading())
        return result;
    }

    function remove(entity) {
        return axios.delete(entityUri + '/' + entity.id, {params: addparams}).then(response => {
            info(i18n.t("crud.deleted.success"))
            return response.data
        }).catch(e => error(e)).finally(() => setLoading())
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
        return axios.get(entityUri, {
            params: queryParams
        }).then(response => {
            return response.data
        }).catch(e => error(e)).finally(() => setLoading())
    }

    return {get, edit, create, save, page, remove};
}

export function restAllServices() {
    axios.get("").then(response => {
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
