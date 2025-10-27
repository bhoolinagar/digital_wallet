export function Button({ children, variant = "default", size = "sm", className = "", ...props }) {
  const baseStyle = "px-3 py-1 rounded font-medium";
  const variantStyle =
    variant === "default"
      ? "bg-blue-600 text-white"
      : "bg-white border border-gray-300 text-gray-700";

  const sizeStyle = size === "sm" ? "text-sm" : "text-base";

  return (
    <button className={`${baseStyle} ${variantStyle} ${sizeStyle} ${className}`} {...props}>
      {children}
    </button>
  );
}