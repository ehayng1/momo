//file down
export function fileDownload(file, fileName) {
  const url = URL.createObjectURL(file)
  const anchor = document.createElement('a')
  const clickHandler = () => {
    setTimeout(() => {
      URL.revokeObjectURL(url)
      anchor.removeEventListener('click', clickHandler)
    }, 150)
  }

  anchor.href = url
  anchor.download = fileName
  anchor.addEventListener('click', clickHandler, false)
  anchor.click()
}
