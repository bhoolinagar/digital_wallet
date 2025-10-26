export function StatCard({ title, value, subtitle }) {
  return (
    <div className="bg-white rounded-lg shadow p-4">
      <h4 className="text-gray-500 text-sm">{title}</h4>
      <p className="text-xl font-bold">{value}</p>
      <span className="text-gray-400 text-xs">{subtitle}</span>
    </div>
  );
}