<script setup>
import { computed } from "vue"
import { getProfileImageSrc } from "@/composables/useProfileImage"

const props = defineProps(['item'])
const emit  = defineEmits(['show-modal'])

const avatarSrc = computed(() => getProfileImageSrc(props.item?.profilePictureReference))

function showModal() {
  emit('show-modal', props.item)
}
</script>

<template>
  <div class="card-root group w-72" @click="showModal">
    <!-- Image area -->
    <div class="relative h-44 overflow-hidden rounded-t-2xl"
         style="background: linear-gradient(135deg, rgba(99,102,241,0.12), rgba(168,85,247,0.08))">
      <img
        class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
        :src="avatarSrc"
        alt="Profile Photo"
      />
      <div class="absolute inset-0 bg-gradient-to-t from-black/20 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
    </div>

    <!-- Content -->
    <div class="p-5">
      <h5 class="text-base font-bold mb-1 truncate" style="color: var(--text-primary)">
        {{ item.firstName + " " + item.lastName }}
      </h5>
      <p class="text-sm mb-4 leading-relaxed" style="color: var(--text-muted)">
        {{ item.personalBiography.substring(0, 65) }}<span v-if="item.personalBiography.length > 65">…</span>
      </p>

      <!-- Stars + badge -->
      <div class="flex items-center gap-2 mb-4">
        <div class="flex gap-0.5">
          <template v-for="n in item.rating" :key="'s' + n">
            <svg class="w-3.5 h-3.5 text-amber-400" fill="currentColor" viewBox="0 0 20 20">
              <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"/>
            </svg>
          </template>
          <template v-for="n in 5 - item.rating" :key="'e' + n">
            <svg class="w-3.5 h-3.5" fill="currentColor" viewBox="0 0 20 20" style="color: var(--border-default)">
              <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"/>
            </svg>
          </template>
        </div>
        <span class="text-xs font-semibold px-2 py-0.5 rounded-full" style="background: rgba(99,102,241,0.1); color: #8b5cf6">
          {{ item.rating }}/5
        </span>
      </div>

      <!-- CTA button -->
      <button
        class="w-full py-2.5 rounded-xl text-sm font-semibold text-white transition-all duration-200 hover:opacity-90 active:scale-[0.98] flex items-center justify-center gap-1.5"
        style="background: var(--gradient-brand)"
      >
        View Profile
        <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/>
        </svg>
      </button>
    </div>
  </div>
</template>

<style scoped>
.card-root {
  border-radius: 16px;
  border: 1px solid var(--border-subtle);
  background: var(--bg-surface);
  box-shadow: var(--shadow-card);
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}
.card-root:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-elevated);
}
</style>
