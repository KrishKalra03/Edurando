export function getProfileImageSrc(profilePictureReference) {
  if (!profilePictureReference) {
    return new URL('../assets/p_placeholder.png', import.meta.url).href
  }
  // Production URL: /api/v1/profileImage/... or absolute http(s)
  if (profilePictureReference.startsWith('/api/') || profilePictureReference.startsWith('http')) {
    return profilePictureReference
  }
  // Legacy local-dev path: ../../../assets/profilePictures/42/foo.png
  const assetsPath = profilePictureReference.match(/assets\/.+/)?.[0]
  if (!assetsPath) {
    return new URL('../assets/p_placeholder.png', import.meta.url).href
  }
  return new URL(`../${assetsPath}`, import.meta.url).href
}
