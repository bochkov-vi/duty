import AXIOS from "@/http_client";
import {error, info, setLoading} from "@/store/store";
import I18N from "@/i18n";


export function restService(entityUri, params) {
    const i18n = I18N;
    const addparams = params ? params : {};



    function get(id) {
        setLoading(true);
        return AXIOS.get(entityUri + "/" + id, {params: addparams}).then((response) => {
            return response.data
        })
            .catch(e => error(e))
            .finally(() => setLoading())
    }

    function edit(entity) {
        setLoading(true);
        return AXIOS.put(entity._links.self.href, entity, {params: addparams}).then(response => {
            return response.data
        }).catch(e => error(e)).finally(() => setLoading())
    }

    function create(entity) {
        setLoading(true)
        return AXIOS.post(entityUri, entity, {params: addparams}).then(response => {
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

        result.then((data) => {
            const msg = i18n.t("crud.saved.success");
            info(msg)
            return data;
        }).finally(() => setLoading())
        return result;
    }

    function remove(entity) {
        return AXIOS.delete(entityUri + '/' + entity.id, {params: addparams}).then(response => {
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
        return AXIOS.get(entityUri, {
            params: queryParams
        }).then(response => {
            return response.data
        }).catch(e => error(e)).finally(() => setLoading())
    }

    return {get, edit, create, save, page, remove};
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
