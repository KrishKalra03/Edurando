<template>
  <Teleport to="body">
    <div class="crop-overlay" @click.self="emit('cancel')">
      <div class="crop-sheet">
        <!-- Header -->
        <div class="crop-header">
          <span class="crop-title">Crop Photo</span>
          <button class="crop-close" @click="emit('cancel')">
            <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.2" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <!-- Hint -->
        <p class="crop-hint">Drag to reposition · Scroll to zoom</p>

        <!-- Cropper image -->
        <div class="crop-canvas-wrap">
          <img ref="imgEl" :src="props.imageSrc" alt="Crop preview" style="max-width:100%;display:block" />
        </div>

        <!-- Actions -->
        <div class="crop-actions">
          <button class="btn-cancel" @click="emit('cancel')">Cancel</button>
          <button class="btn-apply" @click="applyAndEmit">
            <svg width="15" height="15" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
            </svg>
            Apply
          </button>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import Cropper from 'cropperjs'
import 'cropperjs/dist/cropper.css'

const props = defineProps({ imageSrc: String })
const emit  = defineEmits(['apply', 'cancel'])

const imgEl  = ref(null)
let   cropper = null

onMounted(() => {
  cropper = new Cropper(imgEl.value, {
    aspectRatio:        1,
    viewMode:           1,
    dragMode:           'move',
    autoCropArea:       0.75,
    cropBoxMovable:     true,
    cropBoxResizable:   true,
    toggleDragModeOnDblclick: false,
    background:         false,
    guides:             false,
    center:             false,
    highlight:          false,
    movable:            true,
    zoomable:           true,
    scalable:           false,
    rotatable:          false,
  })
})

onUnmounted(() => cropper?.destroy())

function applyAndEmit() {
  const canvas = cropper.getCroppedCanvas({ width: 400, height: 400, imageSmoothingQuality: 'high' })
  canvas.toBlob(blob => emit('apply', blob), 'image/jpeg', 0.92)
}
</script>

<style scoped>
.crop-overlay {
  position: fixed;
  inset: 0;
  z-index: 9999;
  background: rgba(0, 0, 0, 0.72);
  backdrop-filter: blur(6px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
  animation: fade-in 0.18s ease;
}

@keyframes fade-in {
  from { opacity: 0 }
  to   { opacity: 1 }
}

.crop-sheet {
  background: var(--bg-surface);
  border: 1px solid var(--border-default);
  border-radius: 24px;
  width: 100%;
  max-width: 520px;
  box-shadow: 0 32px 80px rgba(0, 0, 0, 0.5);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  animation: slide-up 0.22s cubic-bezier(0.34, 1.3, 0.64, 1);
}

@keyframes slide-up {
  from { transform: translateY(20px) scale(0.97); opacity: 0 }
  to   { transform: translateY(0)    scale(1);    opacity: 1 }
}

.crop-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 18px 20px 14px;
  border-bottom: 1px solid var(--border-subtle);
}

.crop-title {
  font-size: 15px;
  font-weight: 700;
  color: var(--text-primary);
}

.crop-close {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  border: none;
  background: var(--bg-elevated);
  color: var(--text-muted);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.15s, color 0.15s;
}
.crop-close:hover {
  background: rgba(239, 68, 68, 0.1);
  color: #f87171;
}

.crop-hint {
  font-size: 12px;
  color: var(--text-muted);
  text-align: center;
  padding: 8px 0 4px;
}

.crop-canvas-wrap {
  width: 100%;
  max-height: 420px;
  overflow: hidden;
  background: #0d0d14;
}

/* Make crop box circular */
.crop-canvas-wrap :deep(.cropper-view-box) {
  border-radius: 50%;
  outline: 3px solid rgba(139, 92, 246, 0.9);
  outline-offset: -1px;
}
.crop-canvas-wrap :deep(.cropper-face) {
  border-radius: 50%;
  background: transparent;
}
.crop-canvas-wrap :deep(.cropper-dashed),
.crop-canvas-wrap :deep(.cropper-point.point-se)::before {
  display: none;
}
.crop-canvas-wrap :deep(.cropper-point) {
  background: rgba(139, 92, 246, 0.85);
  width: 8px;
  height: 8px;
  border-radius: 50%;
  opacity: 1;
}
.crop-canvas-wrap :deep(.cropper-line) {
  background: transparent;
}

.crop-actions {
  display: flex;
  gap: 10px;
  padding: 16px 20px;
  border-top: 1px solid var(--border-subtle);
}

.btn-cancel {
  flex: 1;
  padding: 11px;
  border-radius: 12px;
  border: 1px solid var(--border-default);
  background: transparent;
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.15s;
}
.btn-cancel:hover { background: var(--bg-elevated); }

.btn-apply {
  flex: 2;
  padding: 11px;
  border-radius: 12px;
  border: none;
  background: var(--gradient-brand);
  color: white;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  transition: opacity 0.15s, transform 0.12s;
}
.btn-apply:hover  { opacity: 0.9; }
.btn-apply:active { transform: scale(0.97); }
</style>
