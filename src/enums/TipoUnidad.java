package enums;

/**
 * Tipos de unidad de la Biblioteca.
 * Jerarquía permitida: BIBLIOTECA -> AREA -> ESTANTE.
 */
public enum TipoUnidad {
    BIBLIOTECA,
    AREA,
    ESTANTE;

    /**
     * Indica si una unidad de este tipo puede tener como hijo a una unidad del tipo dado.
     *
     * @param hijo tipo de la unidad hija
     * @return true si la relación padre -> hijo es válida
     */
    public boolean puedeTenerHijo(TipoUnidad hijo) {
        if (hijo == null) {
            return false;
        }
        switch (this) {
            case BIBLIOTECA:
                return hijo == AREA;
            case AREA:
                return hijo == ESTANTE;
            default:
                return false; // un ESTANTE no tiene unidades hijas
        }
    }
}
