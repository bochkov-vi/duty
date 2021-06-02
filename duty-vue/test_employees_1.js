const traverson = require('traverson-promise')
const JsonHalAdapter = require('traverson-hal');
traverson.registerMediaType(JsonHalAdapter.mediaType, JsonHalAdapter);


console.log("run script test_edit_employee")

let rootUri = 'http://localhost:8080/duty/rest/employees/1'

let payload = {
    id: 1,
    login: null,
    post: 'Техник',
    firstName: 'Василий',
    middleName: 'Степанович',
    lastName: 'Пупкин',
    roadToHomeTime: null,
    _links: {
        self: {href: 'http://localhost:8080/duty/rest/employees/1'},
        item: {
            href: 'http://localhost:8080/duty/rest/employees/1{?projection}',
            templated: true
        },
        shiftTypes: {href: 'http://localhost:8080/duty/rest/employees/1/shiftTypes'},
        employeeGroup: {
            href: 'http://localhost:8080/duty/rest/employees/1/employeeGroup'
        },
        rang: {href: 'http://localhost:8080/duty/rest/employees/1/rang'}
    }
}

// traverson.registerMediaType('text/uri-list',)


const result = traverson.from(rootUri).getResource().resultWithTraversal().then(async ({result, traversal}) => {
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

result.then(r => console.log(r))






