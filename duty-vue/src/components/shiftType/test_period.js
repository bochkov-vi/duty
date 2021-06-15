import {durationAsString, getDuration, getEndTime, totalDuration} from './period.js'

const periods = [{
    start: '09:00:00',
    duration: 'PT4H30M'
}, {
    start: '13:45:00',
    duration: 'PT4H15M'
}, {
    start: '09:00:00',
    duration: 'PT25H'
}, {

}]

function test() {
    for (const period of periods) {
        console.log(period)
        console.log(durationAsString(getDuration(period)))
    }
    console.log(totalDuration(periods))
    console.log(durationAsString(totalDuration(periods)))

    for (const period of periods) {
        console.log(period)
        console.log(getEndTime(period))
    }
}

test()