const tpromise = require('traverson-promise')
const traverson = require('traverson')
const JsonHalAdapter = require('traverson-hal');
traverson.registerMediaType(JsonHalAdapter.mediaType, JsonHalAdapter);
tpromise.registerMediaType(JsonHalAdapter.mediaType, JsonHalAdapter);


console.log("run script test_edit_employee")

let rootUri = 'http://localhost:8080/duty/rest/employees'

let payload = {
    createdDate: null,
    id: 1,
    login: null,
    post: 'Техник',
    firstName: 'Ваня',
    middleName: 'Степанович',
    lastName: 'Пупкин',
    roadToHomeTime: null,
    new: true,
    _links: {
        item: {
            href: 'http://localhost:8080/duty/rest/employees/1{?projection}',
            templated: true
        },
        employeeGroup: {
            href: 'http://localhost:8080/duty/rest/employees/1/employeeGroup'
        },
        shiftTypes: {href: 'http://localhost:8080/duty/rest/employees/1/shiftTypes'},
        rang: {href: 'http://localhost:8080/duty/rest/employees/1/rang'}
    },
    employeeGroup: 'http://localhost:8080/duty/rest/employeeGroups/2',
    shiftTypes: [
        'http://localhost:8080/duty/rest/shiftTypes/1',
        'http://localhost:8080/duty/rest/shiftTypes/2'
    ],
    rang: 'http://localhost:8080/duty/rest/rangs/22'
}


// traverson.registerMediaType('text/uri-list',)

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
        return tpromise.from(url).post(data).resultWithTraversal().then(async ({result, traversal}) => {
            console.log(result.body)
        })
    }
}

function get(url) {
    return tpromise.from(url).getResource().resultWithTraversal().then(async ({result, traversal}) => {
        for (const link in result._links) {
            if (link === "item" || link === "self") {
                continue
            }
            await traversal.continue().follow(link, "self").getResource().result.then((d) => {
                if (d._embedded) {
                    result[link] = d._embedded.items.map((item) => {
                        return item._links.self.href
                    })
                } else
                    result[link] = d._links.self.href

            })
        }
        return result
    })
}

//get()
save(payload,rootUri).then(
    (data) => console.log(data))









