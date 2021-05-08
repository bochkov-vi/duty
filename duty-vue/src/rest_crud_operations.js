import axios from "axios";

export default function createMethods(url) {
    function restGet(id) {
        return axios.get(url + "/" + id).then((response) => response.data)
    }

    function restEdit(entity) {
        return axios.put(url + "/" + entity.id, entity).then(response => response.data)
    }

    function restCreate(entity) {
        return axios.post(url, entity).then(response => response.data)
    }

    function restSave(entity) {
        if (entity.new)
            return restCreate(entity)
        else
            return restEdit(entity)
    }

    function restDelete(entity) {
        return axios.delete(url + '/' + entity.id).then(response => response.data)
    }

    function restPage(options) {
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
        }).then((response) => response.data)
            .catch((error) => console.log(error))
    }

    return {restGet, restEdit, restCreate, restSave, restPage, restDelete};
}