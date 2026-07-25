<script setup>
import { onMounted, ref, nextTick, computed } from 'vue'
import axios from 'axios'
import { useUserStore } from "@/store/store"
import { useWebSocketChat } from "@/composables/useWebSocketChat"
import { getProfileImageSrc } from "@/composables/useProfileImage"

const userStore = useUserStore()
const myId = userStore.getUser.id

const receivers = ref([])
const messages = ref([])
const inputText = ref('')
const selectedReceiverId = ref(userStore.getChatReceiver || null)
const selectedReceiver = ref(null)
const messagesContainer = ref(null)

// ── WebSocket ──────────────────────────────────────────
function onIncomingMessage(msg) {
  const sid = Number(msg.sender)
  const rid = Number(msg.receiver)
  const sel = Number(selectedReceiverId.value)
  const me  = Number(myId)

  if ((sid === me && rid === sel) || (rid === me && sid === sel)) {
    messages.value.push(msg)
    scrollToBottom()
  }
}

const { connect, send, connected } = useWebSocketChat(myId, onIncomingMessage)

// ── Lifecycle ──────────────────────────────────────────
onMounted(async () => {
  await loadContacts()
  if (selectedReceiverId.value) {
    await loadHistory(selectedReceiverId.value)
  }
  connect()
})

// ── Data loading ───────────────────────────────────────
async function loadContacts() {
  try {
    const res = await axios.get(`/chatReceivers/${myId}`)
    receivers.value = res.data
    if (!selectedReceiverId.value && res.data.length > 0) {
      await loadHistory(res.data[0].id)
    }
    // set selectedReceiver object for header display
    syncSelectedReceiver()
  } catch (e) {
    console.error(e)
  }
}

async function loadHistory(receiverId) {
  try {
    selectedReceiverId.value = receiverId
    userStore.chatReceiver = receiverId
    syncSelectedReceiver()
    const res = await axios.get(`/chatHistory/${receiverId}-${myId}`)
    messages.value = res.data
    scrollToBottom()
  } catch (e) {
    console.error(e)
  }
}

function syncSelectedReceiver() {
  selectedReceiver.value = receivers.value.find(r => r.id === selectedReceiverId.value) || null
}

// ── Send ───────────────────────────────────────────────
function sendMessage() {
  const text = inputText.value.trim()
  if (!text || !selectedReceiverId.value) return
  send({ sender: myId, receiver: selectedReceiverId.value, content: text })
  inputText.value = ''
}

function scrollToBottom() {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

function formatTime(ts) {
  if (!ts) return ''
  const d = new Date(ts)
  return d.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}
</script>

<template>
  <div class="chat-root">
    <!-- ── Sidebar ─────────────────────────── -->
    <aside class="chat-sidebar">
      <div class="sidebar-header">
        <h2 class="sidebar-title">Messages</h2>
        <span
          class="ws-dot"
          :class="connected ? 'ws-dot--online' : 'ws-dot--offline'"
          :title="connected ? 'Connected' : 'Connecting…'"
        ></span>
      </div>

      <div class="contacts-list">
        <div
          v-for="r in receivers"
          :key="r.id"
          class="contact-item"
          :class="{ 'contact-item--active': r.id === selectedReceiverId }"
          @click="loadHistory(r.id)"
        >
          <div class="contact-avatar">
            <img :src="getProfileImageSrc(r.profilePictureReference)" alt="Avatar" />
          </div>
          <div class="contact-info">
            <span class="contact-name">{{ r.firstName + " " + r.lastName }}</span>
          </div>
        </div>

        <div v-if="receivers.length === 0" class="contacts-empty">
          <p>No conversations yet</p>
        </div>
      </div>
    </aside>

    <!-- ── Main Chat Area ─────────────────── -->
    <main class="chat-main">
      <!-- Chat header -->
      <div class="chat-header">
        <template v-if="selectedReceiver">
          <div class="chat-header-avatar">
            <img :src="getProfileImageSrc(selectedReceiver?.profilePictureReference)" alt="Avatar" />
            <span class="online-badge" :class="connected ? 'online' : 'offline'"></span>
          </div>
          <div>
            <p class="chat-header-name">{{ selectedReceiver.firstName + " " + selectedReceiver.lastName }}</p>
            <p class="chat-header-status">{{ connected ? 'Online' : 'Connecting…' }}</p>
          </div>
        </template>
        <div v-else class="chat-header-placeholder">
          <p>Select a conversation</p>
        </div>
      </div>

      <!-- Messages -->
      <div class="messages-area" ref="messagesContainer">
        <template v-if="messages.length > 0">
          <div
            v-for="(msg, index) in messages"
            :key="msg.id ?? index"
            class="msg-row"
            :class="Number(msg.sender) === Number(myId) ? 'msg-row--sent' : 'msg-row--received'"
          >
            <div
              class="msg-bubble"
              :class="Number(msg.sender) === Number(myId) ? 'msg-bubble--sent' : 'msg-bubble--received'"
            >
              <span class="msg-text">{{ msg.contents }}</span>
              <span class="msg-time">{{ formatTime(msg.timeSent) }}</span>
            </div>
          </div>
        </template>

        <div v-else-if="selectedReceiverId" class="messages-empty">
          <p>Start the conversation 👋</p>
        </div>

        <div v-else class="messages-empty">
          <p>Select a contact to start chatting</p>
        </div>
      </div>

      <!-- Input bar -->
      <div class="input-bar">
        <input
          v-model="inputText"
          @keyup.enter="sendMessage"
          type="text"
          placeholder="Type a message…"
          class="message-input"
          :disabled="!selectedReceiverId"
        />
        <button
          @click="sendMessage"
          class="send-btn"
          :disabled="!inputText.trim() || !selectedReceiverId"
        >
          <font-awesome-icon :icon="['fas', 'paper-plane']" />
        </button>
      </div>
    </main>
  </div>
</template>

<style scoped>
/* ── Layout ────────────────────────────────────────── */
.chat-root {
  display: flex;
  height: 100vh;
  padding-top: 68px; /* navbar height */
  background: var(--bg-base);
  overflow: hidden;
}

/* ── Sidebar ───────────────────────────────────────── */
.chat-sidebar {
  width: 280px;
  flex-shrink: 0;
  border-right: 1px solid var(--border-subtle);
  background: var(--bg-surface);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.sidebar-header {
  padding: 20px 16px 12px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid var(--border-subtle);
}

.sidebar-title {
  font-size: 16px;
  font-weight: 700;
  color: var(--text-primary);
}

.ws-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  display: inline-block;
}
.ws-dot--online  { background: #4ade80; box-shadow: 0 0 0 2px rgba(74,222,128,0.25); }
.ws-dot--offline { background: #f87171; }

.contacts-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: 12px;
  cursor: pointer;
  transition: background 0.15s ease;
}
.contact-item:hover {
  background: var(--bg-elevated);
}
.contact-item--active {
  background: rgba(99, 102, 241, 0.1);
}
.contact-item--active .contact-name {
  color: #8b5cf6;
}

.contact-avatar img {
  width: 38px;
  height: 38px;
  border-radius: 10px;
  object-fit: cover;
}

.contact-name {
  font-size: 13.5px;
  font-weight: 500;
  color: var(--text-primary);
}

.contacts-empty {
  text-align: center;
  padding: 32px 16px;
  font-size: 13px;
  color: var(--text-muted);
}

/* ── Main Area ─────────────────────────────────────── */
.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  min-width: 0;
}

/* Chat Header */
.chat-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 20px;
  border-bottom: 1px solid var(--border-subtle);
  background: var(--bg-surface);
  flex-shrink: 0;
}

.chat-header-avatar {
  position: relative;
}
.chat-header-avatar img {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  object-fit: cover;
}
.online-badge {
  position: absolute;
  bottom: -2px;
  right: -2px;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  border: 2px solid var(--bg-surface);
}
.online-badge.online  { background: #4ade80; }
.online-badge.offline { background: #94a3b8; }

.chat-header-name {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
  line-height: 1.2;
}
.chat-header-status {
  font-size: 12px;
  color: var(--text-muted);
  margin-top: 1px;
}
.chat-header-placeholder {
  font-size: 14px;
  color: var(--text-muted);
}

/* Messages */
.messages-area {
  flex: 1;
  overflow-y: auto;
  padding: 20px 16px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.messages-empty {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: var(--text-muted);
}

.msg-row {
  display: flex;
  max-width: 70%;
}
.msg-row--sent     { align-self: flex-end; justify-content: flex-end; }
.msg-row--received { align-self: flex-start; }

.msg-bubble {
  padding: 10px 14px;
  border-radius: 16px;
  max-width: 100%;
  position: relative;
}
.msg-bubble--sent {
  background: var(--gradient-brand);
  border-bottom-right-radius: 4px;
}
.msg-bubble--received {
  background: var(--bg-elevated);
  border-bottom-left-radius: 4px;
  border: 1px solid var(--border-subtle);
}

.msg-text {
  display: block;
  font-size: 14px;
  line-height: 1.5;
  word-break: break-word;
}
.msg-bubble--sent     .msg-text { color: #fff; }
.msg-bubble--received .msg-text { color: var(--text-primary); }

.msg-time {
  display: block;
  font-size: 10px;
  margin-top: 4px;
  text-align: right;
  opacity: 0.65;
}
.msg-bubble--sent     .msg-time { color: rgba(255,255,255,0.8); }
.msg-bubble--received .msg-time { color: var(--text-muted); }

/* Input bar */
.input-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  border-top: 1px solid var(--border-subtle);
  background: var(--bg-surface);
  flex-shrink: 0;
}

.message-input {
  flex: 1;
  padding: 11px 16px;
  border-radius: 12px;
  border: 1px solid var(--border-subtle);
  background: var(--bg-elevated);
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
}
.message-input:focus {
  border-color: #8b5cf6;
  box-shadow: 0 0 0 3px rgba(139,92,246,0.10);
}
.message-input::placeholder { color: var(--text-muted); }
.message-input:disabled { opacity: 0.5; cursor: not-allowed; }

.send-btn {
  width: 42px;
  height: 42px;
  border-radius: 12px;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 15px;
  color: white;
  background: var(--gradient-brand);
  transition: opacity 0.2s, transform 0.15s;
  flex-shrink: 0;
}
.send-btn:hover:not(:disabled)  { opacity: 0.88; transform: scale(1.06); }
.send-btn:active:not(:disabled) { transform: scale(0.96); }
.send-btn:disabled { opacity: 0.4; cursor: not-allowed; }
</style>
