/**
 * Resolves a profilePictureReference (stored as a relative path like
 * "../../../assets/profilePictures/42/foo.png") to an absolute URL that
 * works in any component regardless of its depth in the src/ tree.
 * Resolution is always done relative to this composable's location (src/composables/).
 */
export function getProfileImageSrc(profilePictureReference) {
  const assetsPath = profilePictureReference?.match(/assets\/.+/)?.[0]
  if (!assetsPath) {
    return new URL('../assets/p_placeholder.png', import.meta.url).href
  }
  return new URL(`../${assetsPath}`, import.meta.url).href
}
