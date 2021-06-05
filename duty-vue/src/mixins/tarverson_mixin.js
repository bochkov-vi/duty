import traverson from "traverson";

export default function restService(baseUrl, resource) {
    baseUrl = baseUrl ? baseUrl : process.env.REST_BASE_URL;
    const api = traverson.from(baseUrl).follow(resource)

    function post(entity) {
        return new Promise((resolve, reject) => {
            api.post((error, data, t) => {
                if (error)
                    reject(error)
                else
                    resolve(data)
            })
        })
    }

    function put(entity) {
        return new Promise((resolve, reject) => {
            api.post((error, data, t) => {
                if (error)
                    reject(error)
                else
                    resolve(data)
            })
        })
    }


    function getPage(page, size, sort, projection) {
        const promise = new Promise((resolve, reject) => {
            api.withTemplateParameters({
                page: page,
                size: size,
                projection: projection,
                sort: sort
            }).getResource((errormessage, data) => {
                if (errormessage) {
                    reject(errormessage)
                } else {
                    resolve(data)
                }
            })
        })
        return promise;
    }

    function getItem(url) {
        return new Promise(function (resolve, reject) {
            traverson.from(url).getResource(((error, data, traversal) => {
                if (error) {
                    reject(error)
                }
                for (const link in data._links) {
                    if (link === "item" || link === "self") {
                        continue
                    }
                    traversal.continue().follow(link, "self").getResource((d) => {
                        if (d._embedded) {
                            data[link] = d._embedded.items.map((item) => {
                                return item._links.self.href
                            })
                        } else
                            data[link] = d._links.self.href
                    })
                }
                resolve(data)
            }))
        })
    }
}
