let flashSaleId = null;
let flashSale = null
let timeoutStart = null
let timeoutEnd = null
let intervalSecond = null
$(function () {
    $('#flashSale').click(doFlashSale)
    let params = new URL(window.location.href).searchParams
    flashSaleId = params.get("id")
    init()
})

function doFlashSale() {
    let q = $('#num').val()
    let phone = $('#phone').val()
    $.ajax({
        url: SERVER_URL + '/flash-sale/v3?id=' + flashSaleId + '&quantity=' + q  + '&phone=' + phone,
        method: 'POST',
        success: res => {
            if (res.errorCode === 0) {
                alert('秒杀成功')
            } else {
                alert('秒杀失败:' + res.msg)
            }
        }
    })
}

function setButtonActive(active) {
    console.debug("active: ", active)
    if (active) {
        $('#flashSale').attr("disabled", null)
        setCountDown('秒杀中')
    } else {
        $('#flashSale').attr("disabled", "disabled")
    }
}

function clearTimeout() {
    if (timeoutStart != null) {
        clearTimeout(timeoutStart)
    }
    if (timeoutEnd != null) {
        clearTimeout(timeoutEnd)
    }
}

function buttonActive() {
    // 时间判断
    let startTime = new Date(flashSale.startTime)
    let endTime = new Date(flashSale.endTime)
    let now = new Date()
    let intervalToStart = startTime - now
    let intervalToEnd = endTime - now
    if (now < startTime) {
        console.debug("还没开始")
        // 还没开始
        clearTimeout()
        timeoutStart = setTimeout(() => {
            setButtonActive(true)
        }, intervalToStart)
        timeoutEnd = setTimeout(() => {
            setButtonActive(false)
        }, intervalToEnd)
        countDown()
    } else if (now < endTime) {
        console.debug("进行中")
        // 进行中
        clearTimeout()
        timeoutEnd = setTimeout(() => {
            setButtonActive(false)
        }, intervalToEnd)
        setButtonActive(true)
    } else {
        console.debug("结束了")
        // 已经结束
        clearTimeout()
        setButtonActive(false)
    }
}

function setCountDown(txt) {
    $('#countDown').text(txt)
}

function generateCountDownTime(interval) {
    if (interval >= 60 * 60 * 24 * 1000) {
        return '超过一天'
    } else {
        let seconds = Math.floor(interval / 1000) % 60
        let minutes = Math.floor(interval / 1000 / 60) % 60
        let hours = Math.floor(interval / 1000 / 60 / 60) % 24
        return hours + '小时' + minutes + '分钟' + seconds + '秒'
    }
}

function countDown() {
    if (intervalSecond != null) {
        clearInterval(intervalSecond)
    }
    intervalSecond = setInterval(() => {
        let startTime = new Date(flashSale.startTime)
        let now = new Date()
        let interval = startTime - now
        if (interval <= 0) {
            clearInterval(intervalSecond)
        } else {
            setCountDown(generateCountDownTime(interval))
        }
    }, 400)
}


function init() {
    $.ajax({
        url: SERVER_URL + '/flash-sale/detail?id=' + flashSaleId,
        method: 'GET',
        success: res => {
            flashSale = res.data
            for (let key in flashSale) {
                $('#' + key).text(flashSale[key])
            }
            // 处理秒杀
            buttonActive()
        }
    })
}