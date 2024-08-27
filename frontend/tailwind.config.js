/** @type {import('tailwindcss').Config} */
export default {
  // mode: "jit",
  content: ["./src/**/*.{ts,tsx}"],
  theme: {
    extend: {
      colors: {
        primary: "#0046FE",
        secondary: "#E5EBFC",
        base: "#F8F8F8", // 자주 쓰는 회색
        darkBase: "#D9D9D9",
        white: "white",
        enabled: "#4F9F57",
        disabled: "#E94F4F",
        kakao: "#FEE500",
        green: "#4F9F57",
        red: "#E94F4F",
      },
      spacing: {
        sm: "4px",
        md: "8px",
        lg: "12px",
        // 추가 간격 값 정의 가능
      },
      fontFamily: {
        title: ["Lobster", "sans-serif"],
      },
      fontSize: {
        xs: ["12px", "16px"],
        sm: ["14px", "20px"],
        base: ["16px", "24px"], // 폰트 크기, 라인 높이
        lg: ["18px", "28px"],
      },
      fontWeight: {
        thin: 100,
        normal: 400,
        medium: 500,
        bold: 700,
      },
      borderRadius: {
        none: "0px",
        sm: "2px",
        md: "6px",
        lg: "8px",
        full: "9999px",
      },
      borderWidth: {
        default: "1px",
        0: "0",
        2: "2px",
        4: "4px",
      },
      boxShadow: {
        sm: "0 1px 2px 0 rgba(0, 0, 0, 0.05)",
        default:
          "0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)",
        md: "0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)",
        lg: "0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)",
      },
      zIndex: {
        auto: "auto",
        0: "0",
        10: "10",
        20: "20",
        30: "30",
        40: "40",
        50: "50",
      },
      opacity: {
        0: "0",
        25: "0.25",
        50: "0.5",
        75: "0.75",
        100: "1",
      },
      keyframes: {
        explode: {
          "0%": { transform: "scale(0) translate(-50%, -50%)", opacity: "1" },
          "50%": { opacity: "1" },
          "100%": {
            transform:
              "scale(1) translate(var(--x), var(--y)) rotate(var(--rotate))",
            opacity: "1",
          },
        },
        octagonAppear: {
          "0%": {
            opacity: 0,
            transform: "scale(0)",
          },
          "70%": {
            opacity: 1,
            transform: "scale(1.1)",
          },
          "100%": {
            opacity: 1,
            transform: "scale(1)",
          },
        },
        moveBlob1: {
          "0%, 100%": { transform: "translate(0, 0) rotate(0deg) scale(1)" },
          "25%": {
            transform: "translate(20px, -10px) rotate(5deg) scale(1.05)",
          },
          "50%": {
            transform: "translate(-15px, 15px) rotate(-3deg) scale(0.95)",
          },
          "75%": {
            transform: "translate(10px, -10px) rotate(2deg) scale(1.02)",
          },
        },
        moveBlob2: {
          "0%, 100%": { transform: "translate(0, 0) rotate(0deg) scale(1)" },
          "25%": {
            transform: "translate(-25px, 15px) rotate(-6deg) scale(1.03)",
          },
          "50%": {
            transform: "translate(30px, -10px) rotate(4deg) scale(0.97)",
          },
          "75%": {
            transform: "translate(-20px, 20px) rotate(-5deg) scale(1.04)",
          },
        },
        moveBlob3: {
          "0%, 100%": { transform: "translate(0, 0) rotate(0deg) scale(1)" },
          "25%": {
            transform: "translate(15px, 20px) rotate(-4deg) scale(0.96)",
          },
          "50%": {
            transform: "translate(-20px, -15px) rotate(3deg) scale(1.02)",
          },
          "75%": {
            transform: "translate(25px, -25px) rotate(-2deg) scale(1.05)",
          },
        },
        popIn: {
          "0%": { transform: "scale(0.5)", opacity: "0" },
          "80%": { transform: "scale(1.2)", opacity: "1" },
          "100%": { transform: "scale(1)", opacity: "1" },
        },
        fadeInSlideUp: {
          "0%": { opacity: "0", transform: "translateY(20px)" },
          "100%": { opacity: "1", transform: "translateY(0)" },
        },
        buttonActivate: {
          "0%": { transform: "scale(0.95)", opacity: "0.5" },
          "100%": { transform: "scale(1)", opacity: "1" },
        },
        shake: {
          "0%, 100%": { transform: "translateX(0)" },
          "10%, 30%, 50%, 70%, 90%": { transform: "translateX(-2px)" },
          "20%, 40%, 60%, 80%": { transform: "translateX(2px)" },
        },
      },
      animation: {
        explode: "explode 0.8s forwards cubic-bezier(0.25, 0.1, 0.25, 1)",
        octagonAppear:
          "octagonAppear 0.8s forwards cubic-bezier(0.25, 0.1, 0.25, 1)",
        moveBlob1: "moveBlob1 12s ease-in-out infinite",
        moveBlob2: "moveBlob2 10s ease-in-out infinite",
        moveBlob3: "moveBlob3 13s ease-in-out infinite",
        popIn: "popIn 0.5s ease-out forwards",
        fadeInSlideUp: "fadeInSlideUp 0.5s ease-out forwards",
        buttonActivate: "buttonActivate 0.3s ease-out forwards",
        shake: "shake 0.3s ease-in-out",
      },
    },
  },
  plugins: [
    function ({ addUtilities }) {
      const newUtilities = {
        ".scrollbar-hide": {
          "scrollbar-width": "none",
          "-ms-overflow-style": "none",
          "&::-webkit-scrollbar": {
            display: "none",
          },
        },
      };
      addUtilities(newUtilities);
    },
  ],
};
