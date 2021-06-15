import {DateTimeFormatter, Duration, LocalTime} from "@js-joda/core";


export function getDuration(period) {
    return period && period.duration ? Duration.parse(period.duration) : Duration.ZERO
}

export function getStartTime(period) {
    return period && period.start ? LocalTime.parse(period.start) : LocalTime.ofNanoOfDay(0)
}

export function getEndTime(period) {
    const duration = getDuration(period)
    const start = getStartTime(period)
    let secondsOfDay = ((duration.toMillis() / 1000) + start.toSecondOfDay()) % 86400
    const endTime = LocalTime.ofSecondOfDay(secondsOfDay)
    const endStr = endTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    const nextDay = duration.toDays() === 1
    return {end: endStr, nextDay: nextDay}
}

export function durationAsString(duration) {
    let message = ""
    const hours = duration.toHours()
    let h1 = Number.parseInt(hours.toString().slice(-2))
    if (h1 > 0) {
        if (h1 > 20)
            h1 = Number.parseInt(hours.toString().slice(-1))
        if (h1 === 1) {
            message += hours + " час"
        } else if (h1 <= 4 && h1 >= 2) {
            message += hours + " часа"
        } else {
            message += hours + " часов"
        }
    }
    const minutes = duration.toMinutes() % 60
    let m = Number.parseInt(minutes.toString().slice(-2))
    if (m > 0) {
        if (h1 > 0)
            message += " "
        if (m > 20)
            m = Number.parseInt(m.toString().slice(-1))

        if (m === 1) {
            message += minutes + " минута"
        } else if (m <= 4 && m >= 2) {
            message += minutes + " минуты"
        } else {
            message += minutes + " минут"
        }
    }
    return message
}

export function totalDuration(periods) {
    let result = Duration.ZERO
    periods.forEach(p => result = result.plus(getDuration(p)))
    return result
}

export function calculateDuration(start, end, nextDay) {
    let duration = Duration.ZERO
    try {
        const _start = Duration.ofSeconds(LocalTime.parse(start, DateTimeFormatter.ofPattern("HH:mm")).toSecondOfDay())
        const _end = Duration.ofSeconds(LocalTime.parse(end, DateTimeFormatter.ofPattern("HH:mm")).toSecondOfDay())
        const _nextDay = !!nextDay
        if (_start.toMinutes() - _end.toMinutes() < 0) {
            duration = _end.plus(Duration.ofDays(1))
        } else {
            duration = _end
        }
        if (_nextDay) {
            duration = duration.plus(Duration.ofDays(1))
        }
        duration = duration.minus(_start)
    } catch (e) {
        console.debug(e.message)
    }
    return duration
}