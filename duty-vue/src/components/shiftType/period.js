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

export function hourUnitAsString(hours) {
    const h2 = Number.parseInt(hours.toString().slice(-2))
    const h1 = Number.parseInt(hours.toString().slice(-1))
    let unit = "часов"
    if (h1 !== 0 && (h1 < 5 || h2 > 20)) {
        if (h1 === 1) {
            unit = "час"
        } else if (h1 < 5) {
            unit = "часа"
        }
    }
    return unit
}

export function minUnitAsString(minutes) {
    const m2 = Number.parseInt(minutes.toString().slice(-2))
    const m1 = Number.parseInt(minutes.toString().slice(-1))
    let unit = "минут"
    if (m1 !== 0 && (m1 < 5 || m2 > 20)) {
        if (m1 === 1) {
            unit = "минута"
        } else if (m1 < 5) {
            unit = "минуты"
        }
    }
    return unit
}

export function durationAsString(duration) {
    let message = ""
    const hours = duration.toHours()
    const hunit = hourUnitAsString(hours)
    const minutes = duration.toMinutes() % 60
    const munit = minUnitAsString(minutes)
    message += `${hours} ${hunit} ${minutes} ${munit}`;

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
        duration = _end
        if (_nextDay || _end.toMinutes() - _start.toMinutes() <= 0) {
            duration = duration.plus(Duration.ofDays(1))
        }
        duration = duration.minus(_start)
    } catch (e) {
        console.debug(e.message)
    }
    return duration
}
