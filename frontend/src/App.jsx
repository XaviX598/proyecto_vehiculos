import { useState } from 'react'

const API_URL = 'http://localhost:8080/vehiculos'
const FILTROS = ['TODOS', 'RETRO', 'PARADO', 'OK', 'RIESGO', 'CRITICO']

function App() {
  const [vehiculos, setVehiculos] = useState([])
  const [filtro, setFiltro] = useState('TODOS')
  const [placaNueva, setPlacaNueva] = useState('')
  const [velocidades, setVelocidades] = useState({})
  const [cargado, setCargado] = useState(false)
  const [cargando, setCargando] = useState(false)
  const [guardando, setGuardando] = useState(false)
  const [error, setError] = useState('')

  const consultarVehiculos = async (nuevoFiltro) => {
    setCargando(true)
    setError('')

    try {
      const query = nuevoFiltro === 'TODOS' ? '' : `?alerta=${nuevoFiltro}`
      const response = await fetch(`${API_URL}${query}`)

      if (!response.ok) {
        throw new Error('No se pudieron obtener los vehículos')
      }

      const data = await response.json()
      setVehiculos(data)
      setVelocidades(Object.fromEntries(data.map((vehiculo) => [vehiculo.placa, vehiculo.velocidad])))
      setCargado(true)
    } catch (err) {
      setError(err.message)
      setVehiculos([])
    } finally {
      setCargando(false)
    }
  }

  const handleCargar = () => {
    consultarVehiculos(filtro)
  }

  const handleFiltro = (event) => {
    const nuevoFiltro = event.target.value
    setFiltro(nuevoFiltro)

    if (cargado) {
      consultarVehiculos(nuevoFiltro)
    }
  }

  const handleAgregarVehiculo = async () => {
    setGuardando(true)
    setError('')

    try {
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ placa: placaNueva }),
      })

      if (!response.ok) {
        const mensaje = await response.text()
        throw new Error(mensaje || 'No se pudo agregar el vehículo')
      }

      setPlacaNueva('')
      await consultarVehiculos(filtro)
    } catch (err) {
      setError(err.message)
    } finally {
      setGuardando(false)
    }
  }

  const handleEditarVelocidad = async (placa) => {
    setGuardando(true)
    setError('')

    try {
      const response = await fetch(`${API_URL}/${encodeURIComponent(placa)}/velocidad`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ velocidad: Number(velocidades[placa] ?? 0) }),
      })

      if (!response.ok) {
        const mensaje = await response.text()
        throw new Error(mensaje || 'No se pudo actualizar la velocidad')
      }

      await consultarVehiculos(filtro)
    } catch (err) {
      setError(err.message)
    } finally {
      setGuardando(false)
    }
  }

  return (
    <main className="container">
      <h1>Consulta de vehículos</h1>

      <div className="actions">
        <button onClick={handleCargar} disabled={cargando || guardando}>
          {cargando ? 'Cargando...' : 'Cargar vehículos'}
        </button>

        <label>
          Filtro:
          <select value={filtro} onChange={handleFiltro} disabled={cargando || guardando}>
            {FILTROS.map((opcion) => (
              <option key={opcion} value={opcion}>
                {opcion}
              </option>
            ))}
          </select>
        </label>
      </div>

      <div className="add-form">
        <input
          type="text"
          placeholder="Placa"
          value={placaNueva}
          onChange={(event) => setPlacaNueva(event.target.value)}
          disabled={guardando}
        />
        <button onClick={handleAgregarVehiculo} disabled={guardando || !placaNueva.trim()}>
          {guardando ? 'Guardando...' : 'Agregar vehículo'}
        </button>
      </div>

      {error && <p className="error">{error}</p>}

      <table>
        <thead>
          <tr>
            <th>Placa</th>
            <th>Velocidad</th>
            <th>Alerta</th>
            <th>Acción</th>
          </tr>
        </thead>
        <tbody>
          {vehiculos.length === 0 ? (
            <tr>
              <td colSpan="4">{cargado ? 'Sin resultados' : 'Todavía no cargaste datos'}</td>
            </tr>
          ) : (
            vehiculos.map((vehiculo) => (
              <tr key={vehiculo.placa}>
                <td>{vehiculo.placa}</td>
                <td>
                  <input
                    type="number"
                    value={velocidades[vehiculo.placa] ?? 0}
                    onChange={(event) =>
                      setVelocidades((prev) => ({
                        ...prev,
                        [vehiculo.placa]: event.target.value,
                      }))
                    }
                    disabled={guardando}
                  />
                </td>
                <td>{vehiculo.alerta}</td>
                <td>
                  <button onClick={() => handleEditarVelocidad(vehiculo.placa)} disabled={guardando}>
                    Editar velocidad
                  </button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </main>
  )
}

export default App
