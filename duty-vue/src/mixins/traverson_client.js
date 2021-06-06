const traverson = require('traverson')
const JsonHalAdapter = require('traverson-hal');
traverson.registerMediaType(JsonHalAdapter.mediaType, JsonHalAdapter);

export default function restService(baseUrl, resource, item) {
    baseUrl = baseUrl ? baseUrl : process.env.REST_BASE_URL;

    item = item ? item : resource

    function post(entity) {
        return new Promise((resolve, reject) => {
            traverson.from(baseUrl).follow(resource).convertResponseToObject().post(entity, (error, data) => {
                if (error)
                    reject(error)
                else {
                    getItem(data._links.self.href).then((entity) => resolve(entity))
                }
            })
        })
    }

    function del(entity) {
        return new Promise((resolve, reject) => {
            try {
                if (typeof entity === 'string') {
                    resolve(delByUrl(entity))
                } else {
                    resolve(delByUrl(entity._links.self.href))
                }
            } catch (e) {
                reject(e)
            }
        })
    }


    function delByUrl(url) {
        return new Promise((resolve, reject) => {
            try {
                traverson.from(url).delete((err, resp) => {
                    if (err)
                        reject(err)
                    resolve(resp)
                })
            } catch (e) {
                reject(e)
            }
        })
    }

    function put(data, url) {
        return new Promise((resolve, reject) => {
            if (!url) {
                if (data._links && data._links.self) {
                    url = data._links.self.href
                }
            }
            if (url) {
                traverson.from(url).jsonHal().put(data, (error) => {
                    if (error) {
                        reject(error)
                    } else {
                        const links = Object.keys(data._links).filter((link) => (data[link]))
                        Promise.all(links.map(link => putLink(data._links[link].href, data[link]))).then(() => {
                            resolve(data)
                        }).catch(error => reject(error))
                    }
                })
            }
        })

    }

    function putLink(url, value) {
        return new Promise((resolve, reject) => {
            if (value && Array.isArray(value)) {
                value = value.join("\n")
            }
            traverson.from(url).sendRawPayload(true).withRequestOptions({
                headers: {'Content-Type': 'text/uri-list'}
            }).put(value, (error) => {
                if (error) {
                    reject(error)
                } else {
                    resolve()
                }
            })
        })
    }


    function getPage(page, size, sort, projection) {
        return new Promise((resolve, reject) => {
            traverson.from(baseUrl).follow(resource).withTemplateParameters({
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
        const promise = new Promise(function (resolve, reject) {
            traverson.from(url).getResource(((error, data, traversal) => {
                if (error) {
                    reject(error)
                }
                const links = Object.keys(data._links).filter((link) => !(link === item || link === "self"))
                Promise.all(links.map(link => loadLink(data, traversal, link))).then(() => {
                    resolve(data)
                })

            }))
        })
        return promise;
    }


    function loadLink(data, traversal, link) {
        return new Promise((resolve, reject) => {
            try {
                traversal.continue().follow(link, "self").getResource((error, d) => {
                    if (d) {
                        if (d._embedded) {
                            data[link] = d._embedded[link].map((item) => {
                                return item._links.self.href
                            })
                        } else {
                            data[link] = d._links.self.href
                        }
                    }
                    resolve()

                })
            } catch (e) {
                reject(e)
            }
        })
    }

    return {getItem, getPage, post, put, del}
}

