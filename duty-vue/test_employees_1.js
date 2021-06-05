const traverson = require('traverson')
const JsonHalAdapter = require('traverson-hal');
traverson.registerMediaType(JsonHalAdapter.mediaType, JsonHalAdapter);


console.log("run script test_edit_employee")

let rootUri = 'http://localhost:8080/duty/rest/employees'

let payload = {
    "createdDate": null,
    "login": null,
    "post": "Техник",
    "firstName": "Ваня",
    "middleName": "Степанович",
    "lastName": "Пупкин",
    "roadToHomeTime": null,
    "new": true,
    "employeeGroup": "http://localhost:8080/duty/rest/employeeGroups/2",
    "shiftTypes": [
        "http://localhost:8080/duty/rest/shiftTypes/1",
        "http://localhost:8080/duty/rest/shiftTypes/2"
    ],
    "rang": "http://localhost:8080/duty/rest/rangs/22"
}


// traverson.registerMediaType('text/uri-list',)




traverson.from("http://localhost:8080/duty/rest")
    .follow("employees")
    .withTemplateParameters({page:0,size:1,projection:'full-data',sort:'id,desc'}).getResource((error,data,t)=>{
    console.log(data)
})

async function save(data, url) {
    if (data._links && data._links.self) {
        traverson.from(data._links.self.href).put(data, (error, response, traversal) => {
                if (error) {
                    return error
                } else {
                    for (const link in data._links) {
                        if (data[link]) {
                            const associationUrl = data._links[link].href
                            const associationValue = data[link]
                            traverson.from(associationUrl).sendRawPayload(true).withRequestOptions({
                                headers: {'Content-Type': 'text/uri-list'}
                            }).put(associationValue, (error, response) => {
                                if (error) {
                                    console.error(error)
                                }
                            })
                        }
                    }
                }
            }
        )
        return get(data._links.self.href)
    } else {
        const promise = tpromise.from(url).post(data).result


        promise.then(({args}) => {
            console.log()
        }).catch((error) => {
            console.log(error)
        })
    }
}

function get(url) {
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
            resolve({data, traversal})
        }))
    })
}










