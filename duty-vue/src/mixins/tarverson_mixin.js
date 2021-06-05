import traverson from "traverson";

const mixin = {
    props: {
        baseUrl: {
            required: true,
            type: String
        },
        resource: {
            required: true,
            type: String
        }
    },
    data() {
        return {
            item: null,
            page: null,

        }
    }
}

