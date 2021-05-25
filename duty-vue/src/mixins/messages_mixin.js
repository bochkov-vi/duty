import store, {error, info} from "@/store/store";


const mixin = {
    computed: {
        messages: () => {
            return store.getters.GET_MESSAGES
        }
    }, methods: {
        error(msg) {
            error(msg)
        },
        info(msg) {
            info(msg)
        }, removeMessage(msg) {
            this.$store.dispatch('REMOVE_MESSAGE', msg)
        }
    }
}
export default mixin;