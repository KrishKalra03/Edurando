import { ref, onUnmounted } from 'vue'
import { Client } from '@stomp/stompjs'

export function useWebSocketChat(userId, onMessage) {
  const connected = ref(false)
  let client = null

  function connect() {
    client = new Client({
      brokerURL: import.meta.env.VITE_WS_URL,
      reconnectDelay: 5000,
      onConnect: () => {
        connected.value = true
        client.subscribe(`/topic/messages/${userId}`, (frame) => {
          try {
            onMessage(JSON.parse(frame.body))
          } catch (e) {
            console.error('Failed to parse WS message', e)
          }
        })
      },
      onDisconnect: () => {
        connected.value = false
      },
      onStompError: (frame) => {
        console.error('STOMP error', frame)
      }
    })
    client.activate()
  }

  function send(payload) {
    if (client?.connected) {
      client.publish({
        destination: '/app/chat.send',
        body: JSON.stringify(payload)
      })
    } else {
      console.warn('WebSocket not connected — message dropped')
    }
  }

  function disconnect() {
    client?.deactivate()
  }

  onUnmounted(disconnect)

  return { connect, send, disconnect, connected }
}
