import restService from "@/mixins/traverson_client";
import {error, setLoading} from "@/store/store";
import {REST_BASE_URL} from "@/http_client";

export const traverson_mixin = {
    props: {
        resource: {
            required: true,
            type: String
        },
        itemName: {
            required: true,
            type: String
        },
        onPageLoaded: {
            type: Function,
            default: null
        }
    },
    data() {
        return {
            baseUrl: REST_BASE_URL + "/rest",
            item: null,
            page: null,
            rest: null,
            options: null,
            itemToDelete: null
        }
    },
    methods: {
        saveItem() {
            if (this.item) {
                setLoading(true)
                if (this.item._links && this.item._links.self) {
                    this.rest.put(this.item).then(data => this.item = data).catch(e => error(e)).finally(() => setLoading()).then(() => this.onAfterSave())
                } else {
                    this.rest.post(this.item).then(data => this.item = data).catch(e => error(e)).finally(() => setLoading()).then(() => this.onAfterSave())
                }
            }
        },
        onAfterSave() {
            this.editMode = false
            this.loadPage();
        },
        loadPage() {
            setLoading(true)
            const sorts = this.options.sortBy.map(function (srt, index) {
                let result = srt;
                if (this.options.sortDesc[index])
                    result = result + ",desc";
                return result;
            });
            this.rest.getPage(this.options.page - 1, this.options.itemsPerPage, sorts, "full-data")
                .then((data) => {
                    this.page = data;
                    if (this.onPageLoaded) {
                        this.onPageLoaded(data)
                    }
                    return data
                })
                .finally(() => setLoading())
        },
        deleteConfirmed() {
            if (this.item)
                setLoading(true)
            this.rest.del(this.item).then(() => {
                this.confirmDelete = false
                this.item = null
                this.loadPage()
            }).then(() => this.$t("crud.deleted.success")).finally(() => setLoading()).catch((resp) => error(resp.data.message))
        },
        editItem(item) {
            if (item && item._links && item._links.self) {
                setLoading(true)
                this.rest.getItem(item._links.self.href).then((data) => {
                    this.item = data
                }).finally(() => setLoading()).catch(e => error(e))
            } else {
                this.item = null
            }
        },
        deleteItem() {
            setLoading(true)
            this.rest
                .del(this.itemToDelete)
                .then(() => this.loadPage())
                .then(() => this.loadPage())
                .catch(e => error(e.data))
                .then(() => this.deleteMode = false)
                .finally(() => setLoading())
        }
    },
    mounted() {
        this.rest = restService(this.baseUrl, this.resource, this.itemName)

    },
    created() {
        this.page = {_embedded: {}, page: {}}
        this.page._embedded[this.resource] = []
    },
    computed: {
        editMode: {
            get() {
                return this.item != null
            },
            set(val) {
                if (!val) {
                    this.item = null
                }
            }
        },
        deleteMode: {
            get() {
                return this.itemToDelete != null
            },
            set(val) {
                if (!val) {
                    this.itemToDelete = null
                }
            }
        }
    },
    watch: {
        options: {
            deep: true,
            handler() {
                this.loadPage()
            }
        }
    }
}

