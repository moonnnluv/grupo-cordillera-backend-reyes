export default function AdminDashboard({ user }) {
  return (
    <div className="space-y-6">
      <div className="bg-red-50 border border-red-200 rounded-lg p-4">
        <h2 className="text-lg font-semibold text-red-700">Panel de Administración</h2>
        <p className="text-sm text-red-600 mt-1">Acceso completo al sistema</p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <StatCard label="Usuarios activos" value="24" color="bg-blue-100 text-blue-800" />
        <StatCard label="Ventas del mes" value="$1.240.000" color="bg-green-100 text-green-800" />
        <StatCard label="Reportes pendientes" value="3" color="bg-yellow-100 text-yellow-800" />
      </div>

      <div className="bg-white rounded-lg shadow p-4">
        <h3 className="font-semibold text-gray-700 mb-3">Acciones rápidas</h3>
        <div className="flex flex-wrap gap-2">
          <ActionButton label="Gestionar usuarios" />
          <ActionButton label="Ver reportes" />
          <ActionButton label="Configuración del sistema" />
        </div>
      </div>
    </div>
  )
}

function StatCard({ label, value, color }) {
  return (
    <div className={`rounded-lg p-4 ${color}`}>
      <p className="text-sm font-medium">{label}</p>
      <p className="text-2xl font-bold mt-1">{value}</p>
    </div>
  )
}

function ActionButton({ label }) {
  return (
    <button className="bg-gray-100 hover:bg-gray-200 text-gray-700 text-sm px-3 py-2 rounded transition">
      {label}
    </button>
  )
}
