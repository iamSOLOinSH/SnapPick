import { NavLink } from "react-router-dom";
import { BiHomeAlt } from "react-icons/bi";
import { FaRegUser } from "react-icons/fa";
import { IoMdQrScanner } from "react-icons/io";

const NAV_ITEMS = [
  { path: "/", icon: BiHomeAlt },
  { path: "/add", icon: IoMdQrScanner },
  { path: "/profile", icon: FaRegUser },
] as const;

export const BottomTab = () => {
  return (
    <nav
      className="fixed bottom-0 z-30 flex h-16 w-full max-w-md items-center justify-around bg-white px-8 py-3 shadow-xl"
      style={{ boxShadow: "0px -4px 10px rgba(0, 0, 0, 0.1)" }}
    >
      {NAV_ITEMS.map((item) => (
        <NavLink
          key={item.path}
          to={item.path}
          className={({ isActive }) =>
            `flex flex-col items-center ${isActive ? "text-primary" : "text-gray-400"}`
          }
        >
          {item.path === "/add" ? (
            <div className="relative -mt-10 flex items-center justify-center">
              <div
                className="relative z-40 flex h-16 w-16 -translate-y-2 transform items-center justify-center rounded-full bg-primary shadow-lg"
                style={{ boxShadow: "0px -4px 10px rgba(0, 0, 0, 0.1)" }}
              >
                <item.icon size={28} color="white" />
              </div>
            </div>
          ) : (
            <item.icon size={28} />
          )}
        </NavLink>
      ))}
    </nav>
  );
};
