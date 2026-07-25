<script setup>
import axios from "axios"
import { computed, reactive, ref } from "vue"
import { transformData } from "@/functions/functions"
import { useUserStore } from "@/store/store"
import { useRouter } from "vue-router"
import { getProfileImageSrc } from "@/composables/useProfileImage"

const props = defineProps(["item"])
const subjects = reactive(transformData(props.item.topics))
const emit = defineEmits(['close-modal'])
const userStorage = useUserStore()
const router = useRouter()
const result = ref('')
const showError = ref(false)
const avatarSrc = computed(() => getProfileImageSrc(props.item?.profilePictureReference))

function close() {
  emit("close-modal")
}

async function redirectToChat() {
  try {
    await axios.put('/editChatReceivers', { id: userStorage.getUser.id, chatReceiver: props.item.id })
    await router.push('/chat/' + props.item.id)
    await userStorage.fetchChatReceiverById(props.item.id)
  } catch (error) {
    showError.value = true
    result.value = error.response.data
  }
}
</script>

<template>
  <Transition name="modal-fade">
    <div class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <!-- Backdrop -->
      <div
        class="absolute inset-0 bg-black/60 backdrop-blur-sm"
        @click="close"
      ></div>

      <!-- Panel -->
      <div
        class="modal-panel relative w-full max-w-md rounded-3xl border overflow-hidden z-10"
        style="background: var(--bg-surface); border-color: var(--border-default); box-shadow: var(--shadow-elevated)"
      >
        <!-- Top gradient accent -->
        <div class="h-1 w-full" style="background: var(--gradient-brand)"></div>

        <!-- Close -->
        <button
          @click="close"
          class="absolute top-4 right-4 w-8 h-8 flex items-center justify-center rounded-xl border text-sm transition-all hover:scale-110 z-10"
          style="background: var(--bg-elevated); border-color: var(--border-subtle); color: var(--text-muted)"
        >
          <font-awesome-icon :icon="['fas', 'xmark']" />
        </button>

        <!-- Profile header -->
        <div class="flex items-center gap-4 p-6 pb-4">
          <div class="relative flex-shrink-0">
            <img
              class="w-16 h-16 rounded-2xl object-cover"
              :src="avatarSrc"
              alt="Profile Photo"
            />
            <span
              class="absolute -bottom-1 -right-1 w-4 h-4 rounded-full bg-green-400 border-2"
              style="border-color: var(--bg-surface)"
            ></span>
          </div>
          <div class="min-w-0">
            <h3 class="text-lg font-bold truncate" style="color: var(--text-primary)">
              {{ item.firstName + " " + item.lastName }}
            </h3>
            <p class="text-sm mt-0.5 line-clamp-2" style="color: var(--text-muted)">
              {{ item.personalBiography }}
            </p>
          </div>
        </div>

        <!-- Divider -->
        <div class="h-px mx-6" style="background: var(--border-subtle)"></div>

        <!-- Subjects -->
        <div class="p-6 pt-4">
          <p class="text-xs font-semibold uppercase tracking-wider mb-3" style="color: var(--text-muted)">
            Subjects &amp; Topics
          </p>
          <div class="max-h-56 overflow-y-auto space-y-2 pr-0.5">
            <div
              v-for="subject in Object.keys(subjects)"
              :key="subject"
              class="rounded-xl border p-3"
              style="background: var(--bg-elevated); border-color: var(--border-subtle)"
            >
              <p class="text-sm font-semibold mb-2" style="color: var(--text-primary)">{{ subject }}</p>
              <div class="flex flex-wrap gap-1.5">
                <span
                  v-for="topic in subjects[subject]"
                  :key="topic.name"
                  class="text-xs px-2.5 py-1 rounded-full font-medium"
                  style="background: rgba(99,102,241,0.1); color: #8b5cf6"
                >
                  {{ topic.name }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- Footer -->
        <div class="px-6 pb-6">
          <p v-if="showError" class="text-red-400 text-xs mb-3 text-center">{{ result }}</p>
          <button
            @click="redirectToChat"
            class="w-full py-3 rounded-xl text-sm font-semibold text-white transition-all duration-200 hover:opacity-90 hover:scale-[1.02] active:scale-[0.98] flex items-center justify-center gap-2"
            style="background: var(--gradient-brand)"
          >
            <font-awesome-icon :icon="['fas', 'paper-plane']" />
            Contact Me
          </button>
        </div>
      </div>
    </div>
  </Transition>
</template>

<style scoped>
.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity 0.22s ease;
}
.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
}

.modal-panel {
  animation: modal-pop 0.22s cubic-bezier(0.34, 1.56, 0.64, 1);
}
@keyframes modal-pop {
  from { transform: scale(0.94) translateY(12px); opacity: 0; }
  to   { transform: scale(1) translateY(0);       opacity: 1; }
}
</style>
