import restService from "../src/mixins/traverson_client";

describe('traverson_client: ', () => {

    const rest = restService("http://localhost:8080/duty/rest", "employees", "employee")

    it('load entity by url', async () => {
        await rest.getItem("http://localhost:8080/duty/rest/employees/1").then((e) => {
            console.debug(e)
        })
    })

    it('delete entity by url', async () => {
        await rest.del("http://localhost:8080/duty/rest/employees/164").then((e) => {
            console.debug('entity deleted')
        })
    })

    it('post entity', async () => {
        const employee = {
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
        await rest.post(employee).then((entity) => {
            console.debug('entity puted')
            console.debug(entity)
        })
    })

    it('put entity', async () => {
        const employee = {
            "login": null,
            "post": "Страшный инженер",
            "firstName": "Виктор",
            "middleName": "Иванович",
            "lastName": "Бочков",
            "roadToHomeTime": null,
            "new": true,
            "employeeGroup": "http://localhost:8080/duty/rest/employeeGroups/2",
            "shiftTypes": [
                "http://localhost:8080/duty/rest/shiftTypes/3",
                "http://localhost:8080/duty/rest/shiftTypes/4"
            ],
            "rang": "http://localhost:8080/duty/rest/rangs/25",
            _links: {
                self: {href: 'http://localhost:8080/duty/rest/employees/1'},
                employee: {
                    href: 'http://localhost:8080/duty/rest/employees/1{?projection}',
                    templated: true
                },
                rang: {href: 'http://localhost:8080/duty/rest/employees/1/rang'},
                employeeGroup: {
                    href: 'http://localhost:8080/duty/rest/employees/1/employeeGroup'
                },
                shiftTypes: {
                    href: 'http://localhost:8080/duty/rest/employees/1/shiftTypes'
                }
            }
        }
        await rest.put(employee, "http://localhost:8080/duty/rest/employees/1").then((entity) => {
            console.debug('entity puted')
            console.debug(entity)
        })
    })

})
