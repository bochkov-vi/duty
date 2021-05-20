import {error, getLoading, info, setLoading} from "@/store/store";

const mixin = {
    methods: {
        error(message) {
            error(message)
        },
        info(message) {
            info(message)
        }
    },
    computed: {
        loading: {
            get() {
                getLoading()
            },
            set(loading) {
                setLoading(loading)
            }
        }
    }
}
export default mixin;