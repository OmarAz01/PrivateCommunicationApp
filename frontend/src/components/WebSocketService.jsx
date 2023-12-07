import SockJS from 'sockjs-client/dist/sockjs';
import Stomp from 'stompjs';

const SOCKET_URL = 'http://localhost:8080/pm-ws';

class WebSocketService {
  stompClient = null;

  connect(userId, onMessageReceived) {
    const socket = new SockJS(SOCKET_URL);
    const jwt = JSON.parse(localStorage.getItem('user')).jwt;
    this.stompClient = Stomp.over(socket);
    const headers = {
      Authorization: `Bearer ${jwt}`
    };

    this.stompClient.connect(headers, () => {
      this.stompClient.subscribe(`/topic/private`, message => {
        console.log('message receieved' + message);
        const messageBody = JSON.parse(message.body);
        onMessageReceived(messageBody);
      });
    });
  }

  sendMessage(destination, message) {
    console.log('sending message');
    this.stompClient.send(destination, {}, JSON.stringify(message));
  }

  disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
  }
}

export default new WebSocketService();
