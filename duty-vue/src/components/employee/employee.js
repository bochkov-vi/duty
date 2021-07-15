export function getEmployeeFio(item) {
    return (item.rang ? (item.rang.name + " ") : "") + item.lastName + " " + item.firstName + " " + item.middleName
}
