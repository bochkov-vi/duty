import store from './store'

export function setLoading(loading) {
    const value = loading === true
    store.dispatch('SET_LOADING', value);
}

export function getLoading() {
    const loading = store.getters.GET_LOADING
    return loading;
}
