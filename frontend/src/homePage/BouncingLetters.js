let myBubbleArray;
let animationID = null;
// Resizes bubbles when page size is changed
window.addEventListener("resize", init);

class Bubble {
  constructor(color, canvas) {
    this.canvas = canvas;
    this.speed_factor = 3;
    if (canvas.width < 900) {
      this.speed_factor = 1.75;
    }
    this.color = color;
    this.size = Math.random() * canvas.width;
    this.x = Math.random() * canvas.width;
    this.y = Math.random() * canvas.height;
    this.radius = Math.floor(Math.random() * Math.sqrt(canvas.width));
    this.dx = Math.random() * this.speed_factor * randomDirection();
    this.dy = Math.random() * this.speed_factor * randomDirection();
    this.opacity = Math.random();
  }

  drawBubble() {
    const context = this.canvas.getContext("2d");
    context.beginPath();
    context.fillStyle = this.color;
    context.globalAlpha = this.opacity;
    context.arc(this.x, this.y, this.radius, 0, Math.PI * 2, true);
    context.closePath();
    context.fill();
  }
}

function randomDirection() {
  if (Math.random() > 0.5) {
    return -1;
  } else {
    return 1;
  }
}

/**
 * Initializes bubble background by calling the Bubble class
 * and storing those objects inside of myBubbleArray.
 */
function init() {
  // Cancels any previous animation to start a new one
  const canvas = document.getElementById("myCanvas");
  if (animationID !== null) {
    window.cancelAnimationFrame(animationID);
  }
  canvas.width = window.innerWidth;
  canvas.height = window.innerHeight;
  const context = canvas.getContext("2d");
  myBubbleArray = [];
  for (let i = 0; i < 20; i++) {
   let myblue = new Bubble("#4885ed", canvas);
   let myred = new Bubble("#db3236", canvas);
   let mygreen = new Bubble("#3cba54", canvas);
   let myyellow = new Bubble("#f4c20d", canvas);
    myBubbleArray.push(myblue);
    myBubbleArray.push(myred);
    myBubbleArray.push(mygreen);
    myBubbleArray.push(myyellow);
  }
  animationID = window.requestAnimationFrame(animation);
}

let start = null;

function animation(time) {
  if (start === null) {
    start = time;
  }
  let elapsed = time - start;
  move(myBubbleArray, elapsed);
  start = time;
  animationID = window.requestAnimationFrame(animation);
}

/**
 * Moves bubbles in the background and keeps them within the size of the window.
 */
function move(myBubbleArray, timeElapsed) {
  // Clears background with a rectangle
 let canvas = myBubbleArray[0].canvas;
 let context = canvas.getContext("2d");
  context.clearRect(0, 0, canvas.width, canvas.height);
  for (let i = 0; i < myBubbleArray.length; i++) {
   let myBubble = myBubbleArray[i];
    myBubble.drawBubble();
    // Logic to add wall bouncing
    if (myBubble.x >= canvas.width || myBubble.x <= 0) {
      myBubble.dx *= -1;
    }
    if (myBubble.y <= 0 || myBubble.y >= canvas.height) {
      myBubble.dy *= -1;
    }
    myBubble.x += (myBubble.dx * timeElapsed) / 20;
    myBubble.y += (myBubble.dy * timeElapsed) / 20;
  }
}

export { init };