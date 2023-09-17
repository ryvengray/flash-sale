$(function () {
    refresh()
    $('#refresh').click(refresh)
})

function refresh() {
    $.ajax({
        url: SERVER_URL + '/flash-sale/list',
        method: 'GET',
        success: res => {
            let list = res.data
            let trArr = []
            for (let i in list) {
                trArr.push('<tr>')
                trArr.push('<td>' + list[i].id + '</td>')
                trArr.push('<td>' + list[i].name + '</td>')
                trArr.push('<td>' + list[i].quantity + '</td>')
                trArr.push('<td>' + list[i].startTime + '</td>')
                trArr.push('<td>' + list[i].endTime + '</td>')
                trArr.push('<td><a class="btn btn-link" href="./pages/detail.html?id=' + list[i].id + '">秒杀</a></td>')
                trArr.push('</tr>')
            }
            $('#table_body').html(trArr.join(''))
        }
    })
}