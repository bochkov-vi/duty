import traverson from "traverson";

export function restService(baseUrl, resource) {
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


    function put(data, url) {
        if (!url)
            if (data._links && data._links.self) {
                url = data._links.self.href
            }
        traverson.from(url).put(data, (error, response, traversal) => {
            if (error) {
                reject(error)
            } else {
                for (const link in data._links) {
                    if (data[link]) {
                        const associationUrl = data._links[link].href
                        const associationValue = data[link]
                        traverson.from(associationUrl).sendRawPayload(true).withRequestOptions({
                            headers: {'Content-Type': 'text/uri-list'}
                        }).put(associationValue, (error, response) => {
                            if (error) {
                                reject(error)
                            }
                        })
                    }
                }
            }
        })
    }


    function getPage(page, size, sort, projection) {
        return new Promise((resolve, reject) => {
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

    return {getItem, getPage, post, put}
}

