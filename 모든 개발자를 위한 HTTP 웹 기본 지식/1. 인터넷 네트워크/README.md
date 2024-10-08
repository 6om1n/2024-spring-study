# ⭐ 1. 인터넷 네트워크
## IP(인터넷 프로토콜)

> 이렇게 복잡한 인터넷에서 목적지까지 안전하게 도착하려면 어떻게 해야할까?
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/df0828b9-006e-4b19-920b-27734029906e)

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/9b739efa-40aa-4309-8dd3-6324c18af336)

- IP 주소르 이용한다. 
- IP 인터넷 프로토콜의 역할
    -  지정한 IP 주소 (IP Address)에 데이터 전달
    - 패킷 (Packet) 이라는 통신 단위로 데이터 전달

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/4d6d9ea2-c499-48d8-a742-a9510c14137d)

- IP 패킷에는 출발지 IP, 목적지 IP와 전송할 데이터가 들어가있다. 
- 클라이언트는 출발지 IP, 목적지 IP, 메시지를 넣고 넘기면 그 목적지를 찾아서 도착한다. 
- 사진에는 클라이언트만 나와있지만, 서버도 마찬가지로 잘 받았다는 내용을 담아 IP 패킷을 만들어서 서버쪽에서 전달한다. 
- 인터넷 망을 복잡한 상태이기 때문에 경로는 다르게 진행될 수 있다. 

#### IP 프로토콜의 한계
- 비연결성
> 패킷을 받을 대상이 없거나 서비스 불능 상태여도 패킷을 전송한다.
> 서버의 포트번호가 살아있는지 모른 채로, 보내는 문제점이 발생할 수 있다.  
- 비신뢰성         
> 송신자가 전송한 패킷의 손상여부를 송/수신자가 알 수 없으며, 패킷 전달 순서가 보장되지 않는다. 
> 패킷들이 서로 다른 노드들을 타고 목적지로 도착하기 때문에 서버에 순서가 다르게 도착할 수 있는 문제점이 존재한다. 
- 프로그램 구분
> 같은 IP를 사용하는 서버에서 통신하는 애플리케이션이 둘 이상이면 문제가 발생한다. 

### :round_pushpin: 결론
이렇게 IP 프로토콜은 많은 문제점이 있다. 따라서 이를 해결할 수 있는 TCP를 많이 사용한다. 
***

## TCP, UDP
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/a8ebb317-e976-4db5-8dab-65d0501ce8c1)

- 인터넷 프로토콜 스택의 4계층
> - 애플리케이션 계층 - HTTP, FTP
> - 전송 계층 - TCP, UDP
> - 인터넷 계층 - IP
> - 네트워크 인터페이스 계층

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/b11d0b30-6042-4d9d-992c-e31261a8b9cb)

> 프로그램이 메시지를 전달하면, tcp 계층에서 한 겹 쌓고, IP 계층에서도 한 겹 쌓고, 마지막으로 이더넷 프레임이 씌워져서 나가게 된다.  

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/cac1b5ec-a619-45a9-b6f9-c333a7ba370d)

> IP 문제만으로 해결이 안되었던 것이 출발지 PORT, 목적지 PORT, 전송 제어, 순서 등등의 정보를 가지고 전달한다. 

#### TCP (전송 제어 프로토콜) 특징
- 연결 지향 - 3 way handshake (가상 연결)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/7ca517f5-58fb-44be-b91b-2bbeb0ec3845)

> - 연결을 한 다음에 메시지를 전송한다. 
> - 클라이언트에서 접속 요청인 SYN 을 보내면 서버에서 SYN+ACK 를 전송하고, 다시 그걸 받은 클라이언트가 ACK 를 보낸다. 이렇게 3번을 전송해서 3 way handshake 라고 부른다. 
> - 원래는 3번을 주고 받고 난 이후에 데이터를 전송하는데, 최근에는 최적화 되어서 3번째 ACK를 보낼 때 메시지를 같이 전송하기도 한다. 
- 데이터 전달 보증
- 순서 보장
> - 클라이언트에서 서버로 전송하는 과정에서 순서가 바뀔 수 있는데 순서가 바뀌어서 도착 할 경우 서버에서 몇 번부터 다시 보내라고 전달한다. 따라서 순서가 보장된다. 
- 신뢰할 수 있는 프로토콜
- 현재는 대부분 TCP 사용

#### UDP(사용자 데이터그램 프로토콜) 특징
- 기능이 거의 없고, TCP 에서 되었던 특징들이 모두 안된다. 
- UDP는 IP와 거의 같은 역할을 한다. 
- 거기에 더하여 PORT + 체크섬 정도만 추가한다. 
- 애플리케이션에서 추가적으로 작업이 더 필요하다. 
***

## PORT
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/e9de8270-ac92-409c-a217-87d02d67d627)

- 이렇게 같은 Ip 내에서 한 번에 둘 이상 연결해야할 때는 어떻게 해야할까?
- PORT 를 사용하여 같은 IP 내에서 프로세스를 구분한다. 
- 즉, IP는 아파트 PORT 는 아파트 내 동 으로 생각하면 된다. 
- 포트는 0부터 65535까지 할당 가능하고, 0~1023번은 잘 알려진 포트여서 사용하지 않는 것이 좋다. 
> - FTP -20,21
> -  TELNET - 23
> -  HTTP - 80
> - HTTPS - 443
***

## DNS
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/bc7ada7c-d251-423a-8798-0d1afb1e1f47)

> - IP는 기억하기 어렵고, 변경될 수 있다. 따라서 DNS를 사용할 수 있다. 

#### DNS (도메인 네임 시스템)
- 전화번호부
- 도메인 명을 IP 주소로 변환

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/c2e389b1-681a-4f76-bdd7-bfb93d5aeff7)

> - 클라이언트에서 도메인 명으로 찾으면 DNS 를 활용해서 도메인 명에 해당하는 IP 주소를 응답하고, 그 IP주소로 서버에 응답하면 된다. 

***
## 🔗출처

- 김영한 - [모든 개발자를 위한 HTTP 웹 기본 지식](https://www.inflearn.com/course/http-웹-네트워크/dashboard)

***
