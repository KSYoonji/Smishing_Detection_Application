# 스미싱 탐지 어플
- 스미싱 문자를 탐지하는 어플입니다. 문자가 수신될 때마다 스미싱 문자일 확률을 구하여 사용자에게 알림을 보냅니다. <br/>

#### 💨 **개발 플랫폼** : Android Studio <br/>
#### 💨 **개발 언어** : Java<br/>
#### 💨 **데이터베이스** : Firebase<br/>
#### 💨 **딥러닝 프레임워크** : 텐서플로우<br/>
----
#### 💫 탐지 알고리즘<br/>
<img src="https://user-images.githubusercontent.com/44793355/133667157-3bcdf39b-2d3c-4970-b559-b6b4aa7ab3c1.png" width="80%" height="80%"/>

<br/><br/>

#### 👁‍🗨 시연영상 _(AI 부분 미탑재. 랜덤 값을 대신 넣어놓은 상태)_ 
- 114 on DB에 존재하지 않은 전화번호가 포함된 문자일 경우 : '주의'표시
- url이 포함된 문자일 경우 : AI 탐지 퍼센트 표시 (예: 98%) <br/>
<img src="https://user-images.githubusercontent.com/44793355/133685276-201d8d9c-a732-483d-9c14-bad4a6c2f893.gif" width="30%" height="30%"/> <br/><br/>

- 114 on DB에 존재하지 않은 전화번호가 포함된 문자 or url이 포함된 문자일 경우 : 알림0
- 114 on DB에 존재하는 전화번호가 포함된 문자일 경우 : 알림X <br/>
<img src="https://user-images.githubusercontent.com/44793355/133690485-58a50487-0356-4e71-8d15-1ff12863c834.jpg" width="30%" height="30%"/> <br/>
<img src="https://user-images.githubusercontent.com/44793355/133690499-ecf85637-ff3b-459a-aac8-88c3049fd7cb.jpg" width="30%" height="30%"/> <br/>
<img src="https://user-images.githubusercontent.com/44793355/133692506-a7cba1af-932a-41aa-a78e-ebaec168613a.png" width="30%" height="30%"/> <br/>

*개인정보 보호를 위해 전화번호는 바꾸거나 가림
