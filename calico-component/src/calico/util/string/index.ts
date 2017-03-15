export function randomString(length: number, chars?: string): string {
  if(chars == null){
    chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  }
  let clen = chars.length;
  let ret = "";
  for(let i=0; i < length; i++){
    ret += chars[Math.floor(Math.random() * clen)];
  }
  return ret;
}
