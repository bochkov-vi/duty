const minilog = require('./node_modules/minilog/lib/index.js').pipe(process.stdout);

const traverson = require('traverson')
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

traverson.from(rootUri).get((error, document, traversal) => {
    if (error) {
        console.log(error)
        return done(error)
    } else {
        console.log(document.body)
    }
    traversal.continue().follow("rang").get((error, document) => {
        if (error) {
            console.log(error)
        } else {
            console.log("get Rang")
            console.log(document.body)
        }
    })
    traversal.continue().follow("rang")
        .withRequestOptions({
            headers: {'Content-Type': 'text/uri-list'}
        })
        .put({body:"http://localhost:8080/duty/rest/rangs/12"}, (error, document) => {
            if (error)
                console.log(error)
            else
                console.log(document.body)
        })
})





