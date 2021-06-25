# petdora
## 目錄
1.[簡介](#簡介)   
2.[架構](#架構)   
3.[技術](#技術)   
4.[系統文件](#系統文件) 
## 簡介
```sh
此App是一款以寵物為主的社群軟體，使用者可以透過程式向好友們發布寵物相關的貼文，讓其他使用者留言、分享，並且會在每天固定時間進行好友抽卡，擴增
使用者的好友圈，除此之外使用者還可以瀏覽與寵物相關的活動，並透過此App進行報名便能與寵物一同參加活動，雙向寵物翻譯功能，讓使用者與寵物平時的
相處中能更添樂趣，讓飼主能了解寵物想表達的意思並加深與寵物間的羈絆。
```
<div class="image" style="display:flex">
<img src="https://i.imgur.com/DBir1VH.jpg" alt="Editor" width="22%"/>
<img src="https://i.imgur.com/MNqO6s1.jpg" alt="Editor" width="22%"/>
<img src="https://i.imgur.com/nazHQMm.jpg" alt="Editor" width="22%"/>
<img src="https://i.imgur.com/MNibAGK.jpg" alt="Editor" width="22%"/>
</div>  
  
## 架構
| 主要頁面 | 主要功能 |
|----------|----------|
| 首頁     | 顯示所有生活照(好友、全部)   |
| 生活照頁面     | 查看生活照貼文<br>發布生活照貼文<br>回應貼文   |
| 活動頁面     | 查看活動<br>發布活動<br>參加活動   |
| 翻譯頁面     | 寵物聊天室   |
| 聊天室頁面     | 聊天記錄<br>群組列表<br>好友列表   |
| 通知頁面     |  貼文回應  |
| 個人頁面     |  個人資料<br>寵物資料<br>抽卡設置|
## 技術
1.DB：透過 firebase 作為連結整個 APP 的資料庫  
2.API：使用google api協助頁面呈現  
3.socket：協助client與server資料傳遞  
4.Programing Language：Java、Python
## 系統文件
[企劃文件](https://docs.google.com/document/d/1_QcGSgNUrKzMmxpHispQ2iEHkTEPtVYB/export?format=docx)
