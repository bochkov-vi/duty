import axios from "axios";


function page(options) {
    return axios.get(url, {params: {...options, projection: "full-data"}})
}

function getById(url, id) {
    return getByUrl(`${url}/${id}`)
}

function getByUrl(url) {
    return axios.get(url).then(async (response) => {
        const data = response.data
        const links = data._links
        if (links)
            for (const link in links) {
                if (link == "item" || link === "self")
                    continue;
                else {
                    await axios.get(links[link].href).then((chr) => {
                        const child = chr.data;
                        if (child._embedded) {
                            data[link] = child._embedded.items.map((el) => el._links.item.href)
                        } else {
                            data[link] = child._links.item.href
                        }
                        return data
                    }).catch(e => console.log(e))
                }

            }
        return data;
    }).catch((e) => console.log(e))
}


export default {getByUrl, getById};