const main = {
    init: function() {
        const _this = this;
        $('#btn-save').on('click', (e) => {
            e.stopPropagation()
            _this.save();
        })

        $('#btn-update').on('click', (e) => {
            e.stopPropagation()
            _this.update();
        })

        $('#btn-delete').on('click', (e) => {
            e.stopPropagation()
            _this.delete();
        })
    },
    save: function() {
        const data = {
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val()
        }

        $.ajax({
            type: 'post',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        })
        .done(() => {
            alert('글이 등록되었습니다')
            window.location.href = "/"
        })
        .fail((error) => {
            alert(JSON.stringify(error))
        })
    },
    update: function () {
        const data = {
            title: $('#title').val(),
            content: $('#content').val()
        }

        const id = $('#id').val();

        $.ajax({
            type: 'put',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        })
        .done(() => {
            alert('글이 수정되었습니다.')
            window.location.href = "/"
        })
        .fail((error) => {
            alert(JSON.stringify(error))
        })
    },
    delete: function () {
        const id = $('#id').val();

        $.ajax({
            type: 'delete',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
        })
        .done(() => {
            alert('글이 삭제되었습니다..')
            window.location.href = "/"
        })
        .fail((error) => {
            alert(JSON.stringify(error))
        })
    }
}

main.init();