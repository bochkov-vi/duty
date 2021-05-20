import axios from "axios";

export const REST_BASE_URL = process.env.REST_BASE_URL || "http://localhost:8080/duty/rest"
const AXIOS = axios.create({baseURL: REST_BASE_URL})
const isoDateFormat = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(?:\.\d*)?$/;

function isIsoDateString(value) {
    return value && typeof value === "string" && isoDateFormat.test(value);
}

export function handleDates(body) {
    if (body === null || body === undefined || typeof body !== "object")
        return body;
    for (const key of Object.keys(body)) {
        const value = body[key];
        if (isIsoDateString(value)) body[key] = new Date(value);
        else if (typeof value === "object") handleDates(value);
    }
}

AXIOS.interceptors.response.use(originalResponse => {
    handleDates(originalResponse.data);
    return originalResponse;
});


export default AXIOS;