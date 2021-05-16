import axios from "axios";
import {error, setLoading} from "@/store/store";


export default function restService(url) {
    function get(id) {
        setLoading(true);
        return axios.get(url + "/" + id).then((response) => {
            setLoading();
            return response.data
        }).catch(e => error(e))
    }

    function edit(entity) {
        setLoading(true);
        return axios.put(url + "/" + entity.id, entity).then(response => {
            setLoading();
            return response.data
        }).catch(e => error(e))
    }

    function create(entity) {
        return axios.post(url, entity).then(response => {
            setLoading();
            return response.data
        }).catch(e => error(e))
    }

    function save(entity) {
        if (entity.new)
            return create(entity)
        else
            return edit(entity)
    }

    function remove(entity) {
        return axios.delete(url + '/' + entity.id).then(response => {
            setLoading();
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
        return axios.get(url, {
            paramsSerializer(params) {
                const searchParams = new URLSearchParams();
                for (const key of Object.keys(params)) {
                    const param = params[key];
                    if (Array.isArray(param)) {
                        for (const p of param) {
                            searchParams.append(key, p);
                        }
                    } else {
                        searchParams.append(key, param);
                    }
                }
                return searchParams.toString();
            },
            params: queryParams
        }).then(response => {
            setLoading();
            return response.data
        }).catch(e => error(e))
    }

    return {get: get, edit: edit, create: create, save: save, page: page, remove: remove, restRemove: remove};
}
