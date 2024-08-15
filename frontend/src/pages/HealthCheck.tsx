import { useEffect, useState } from "react";
import { isConnected } from "../utils/api";

const HealthCheck = () => {
  const [message, setMessage] = useState<string>("no........");

  useEffect(() => {
    const getMessage = async () => {
      const response = await isConnected();
      setMessage(response.data);
    };
    getMessage();
  }, []);
  return <div>Is this connected to the server❓{message}</div>;
};

export default HealthCheck;
