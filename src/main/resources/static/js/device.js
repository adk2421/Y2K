/* 기기 관련 스크립트 */

// 기기 추가
const btnAdd = document.getElementById('btnAdd');
btnAdd.addEventListener('click', () => {
    const dvcType = document.getElementById('dvcTypeSelect').value;
    const dvcBrc = document.getElementById('dvcBrcAddInput').value;

    if (dvcBrc === '') {
        alert('지점명을 입력해 주세요.');
        return false;
    }

    const deviceModel = {
        'dvcType' : dvcType,
        'dvcBrc' : dvcBrc
    }

    $.ajax({
        type: 'POST',
        url: '/addDvc',
        data: deviceModel,
        dataType: 'json',
        success: (data) => {
            console.log(data);
            alert("기기가 추가되었습니다.");
            location.replace("");
        },
        error: function (error) {
            // 요청이 실패한 경우
            console.log('Error:', error);
        }
    })
})

// 기기 수정 모달 실행 시
// const deviceModifyModal = document.getElementById('deviceModifyModal')
// deviceModifyModal.addEventListener('show.bs.modal', () => {
//     let chkCnt = 0;
    
//     let chkBoxList = document.getElementsByClassName('chkBox');
//     Array.prototype.forEach.call(chkBoxList, (chkBox) => {
//         if (chkBox.checked) {
//             chkCnt++;

//             if (chkCnt > 1) {
//                 alert('수정은 하나의 항목만 가능합니다.');
//                 deviceModifyModal.close;
//                 return false;
//             }
//         }
//     });
// })

// 기기 수정
const btnModify = document.getElementById('btnModify');
btnModify.addEventListener('click', (mouseEvent) => {
    let chkItem = '';
    let chkCnt = 0;

    // console.log(mouseEvent.target);
    let chkBoxList = document.getElementsByClassName('chkBox');
    Array.prototype.forEach.call(chkBoxList, (chkBox) => {
        if (chkBox.checked) {
            chkItem = chkBox.id;
            chkCnt++;
        }
    });

    if (chkCnt < 1) {
        alert('수정할 항목을 체크해주세요.');
        return false;
    }

    if (chkCnt > 1) {
        alert('하나의 항목만 수정 가능합니다.');
        return false;
    }

    const dvcBrc = document.getElementById('dvcBrcModifyInput').value;
    if (dvcBrc === '') {
        alert('지점명을 입력해 주세요.');
        return false;
    }

    const deviceModel = {
        'dvcId' : chkItem,
        'dvcBrc' : dvcBrc
    }

    $.ajax({
        type: 'PUT',
        url: '/modifyDvc',
        data: deviceModel,
        dataType: 'json',
        success: (data) => {
            console.log(data);
            alert("기기가 수정되었습니다.");
            location.replace("");
        },
        error: function (error) {
            // 요청이 실패한 경우
            console.log('Error:', error);
        }
    })
})

// 기기 삭제
const btnRemove = document.getElementById('btnRemoveDvc');
btnRemove.addEventListener('click', () => {
    let chkItems = [];
    let chkCnt = 0;

    // console.log(mouseEvent.target);
    let chkBoxList = document.getElementsByClassName('chkBox');
    Array.prototype.forEach.call(chkBoxList, (chkBox) => {
        if (chkBox.checked) {
            chkItems.push(chkBox.id);
            chkCnt++;
        }
    });

    if (chkItems.length === 0) {
        alert('삭제할 항목을 체크해주세요.');
        return false;
    }

    $.ajax({
        type: 'PUT',
        url: '/removeDvc',
        data: { 'dvcIdArr' : chkItems },
        dataType: 'json',
        traditional: true,
        success: (data) => {
            console.log(data);
            alert("기기가 삭제되었습니다.");
            location.replace("");
        },
        error: function (error) {
            // 요청이 실패한 경우
            console.log('Error:', error);
        }
    })
})