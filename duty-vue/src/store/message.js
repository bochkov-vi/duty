import store from './store'

export default function error(msg) {
    const message = {message: msg, type: 'error'};
    console.error(message)
    store.dispatch("ADD_MESSAGE", message)
}