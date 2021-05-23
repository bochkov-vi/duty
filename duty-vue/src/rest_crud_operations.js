import axios from "@/http_client";
import {error, info, setLoading} from "@/store/store";
import I18N from "@/i18n";


export function restService(entityUri) {
    const i18n = I18N;

    function get(id) {
        setLoading(true);
        return axios.get(entityUri + "/" + id).then((response) => {
            setLoading();
            return response.data
        }).catch(e => error(e))
    }

    function edit(entity) {
        setLoading(true);
        return axios.put(entityUri + "/" + entity.id, entity).then(response => {
            setLoading();
            return response.data
        }).catch(e => error(e))
    }

    function create(entity) {
        return axios.post(entityUri, entity).then(response => {
            setLoading();
            return response.data
        }).catch(e => error(e))
    }

    function save(entity) {
        let result;
        info(entity)
        if (entity.new)
            result = create(entity)
        else
            result = edit(entity)

        result.then(function (data) {
            const msg = i18n.t("crud.saved.success");
            info(msg)
            return data;
        })
        return result;
    }

    function remove(entity) {
        return axios.delete(entityUri + '/' + entity.id).then(response => {
            setLoading();
            info(i18n.t("crud.deleted.success"))
            return response.data
        }).catch(e => error(e))
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
            sort: sorts
        };
        setLoading(true)
        return axios.get(entityUri, {
            params: queryParams
        }).then(response => {
            setLoading();
            return response.data
        }).catch(e => error(e))
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
