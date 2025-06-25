import { Injectable } from '@angular/core';
import { Client, StompSubscription, IMessage } from '@stomp/stompjs';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private stompClient: Client | null = null;
  private subscription: StompSubscription | null = null;
  private notificationsSubject = new BehaviorSubject<any[]>([]);
  public notifications$ = this.notificationsSubject.asObservable();
  private userId: number | null = null;

  connect(userId: number) {
    // this.userId = userId;
    // if (this.stompClient) {
    //   this.disconnect();
    // }
    // this.stompClient = new Client({
    //   brokerURL: 'ws://localhost:8087/ws/notifications',  // NATIV!
    //   debug: (str) => console.log('STOMP Debug:', str),
    //   reconnectDelay: 5000,
    //   heartbeatIncoming: 4000,
    //   heartbeatOutgoing: 4000,
    // });

    // this.stompClient.onConnect = () => {
    //   if (this.stompClient && this.userId) {
    //     this.subscription = this.stompClient.subscribe(
    //       `/topic/notifications/${this.userId}`,
    //       (message: IMessage) => {
    //         const notif = JSON.parse(message.body);
    //         this.notificationsSubject.next([notif, ...this.notificationsSubject.value]);
    //       }
    //     );
    //   }
    // };

    // this.stompClient.onStompError = (frame) => {
    //   console.error('STOMP error:', frame);
    // };

    // this.stompClient.onWebSocketError = (error) => {
    //   console.error('WebSocket error:', error);
    // };

    // this.stompClient.onWebSocketClose = (event) => {
    //   console.error('WebSocket closed:', event);
    // };

    // this.stompClient.activate();
  }

  disconnect() {
    // if (this.stompClient) {
    //   this.stompClient.deactivate();
    //   this.stompClient = null;
    // }
    // this.notificationsSubject.next([]);
  }
}
