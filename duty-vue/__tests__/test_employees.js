import rest from "../src/hateoas";

describe('hateoas: ', () => {


    it('load entity by url', () => {
        rest.getByUrl("http://localhost:8080/duty/rest/employees/1")
            .then((data) => console.log(data))
    })

    it('load entity by url', () => {
        rest.getByUrl("http://localhost:8080/duty/rest/shiftTypes/1")
            .then((data) => console.log(data))
    })


})
