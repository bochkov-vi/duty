import {restService} from "@/rest_crud_operations";

export const crudRestMixin = {
    data() {
        return {
            service: {},
            editMode: false
        }
    },
    props: {
        id: null,
        uri: {
            required: true
        }
    }, methods: {
        resetItem() {
            this.item = {}
        },
        editItem(item) {
            this.item = item
        },
        saveItem() {
            this.service.save(this.item).then((saved) => this.item = saved)
        },
        deleteItem() {
            this.service.remove(this.item).then(() => this.resetItem())
        },
        deleteConfirmed() {
            this.service.remove(this.item).then(() => this.resetItem())
        },
        loadItem(id) {
            if (id)
                this.service.get(id).then((loaded) => this.item = loaded)
        },
        refreshItem() {
            this.id = this.item.id
            this.loadItem()
        },
        loadPage() {
            this.service.page(this.options).then(data => this.page = data)
        }
    },
    mounted() {
        this.service = restService(this.uri)
    }, watch: {
        options: {
            handler() {
                this.loadPage()
            },
            deep: true,
        },
        id: () => {
            if (this.id > 0) {
                this.loadItem(id)
                this.editMode = true
            } else if (this.id < -1) {
                this.item = {}
                this.editMode = true
            } else {
                this.editMode = false;
                this.item = {}
            }
        }
    }

}


