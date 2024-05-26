package org.jimmybobjim.circuitcraft.api.chemistry;

@SuppressWarnings("unused")
public class Compounds {
    public static final Compound
            WATER = Compound.of(
                    CompoundComponent.of(Elements.HYDROGEN, 2),
                    CompoundComponent.of(Elements.OXYGEN)
            ),
            HEAVY_WATER = Compound.of(
                    CompoundComponent.of(Isotopes.DEUTERIUM, 2),
                    CompoundComponent.of(Elements.OXYGEN)
            ),
            AMMONIA = Compound.of(
                    CompoundComponent.of(Elements.NITROGEN),
                    CompoundComponent.of(Elements.HYDROGEN, 3)
            ),
            METHANE = Compound.of(
                    CompoundComponent.of(Elements.CARBON),
                    CompoundComponent.of(Elements.HYDROGEN, 4)
            );

    public static class Acids {
        public static final Compound
                SULFURIC_ACID = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN, 2),
                        CompoundComponent.of(PolyatomicIons.SULFATE)
                ),
                SULFUROUS_ACID = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN, 2),
                        CompoundComponent.of(PolyatomicIons.SULFITE)
                ),
                NITRIC_ACID = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN),
                        CompoundComponent.of(PolyatomicIons.NITRATE)
                ),
                NITROUS_ACID = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN),
                        CompoundComponent.of(PolyatomicIons.NITRIDE)
                ),
                PHOSPHORIC_ACID = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN, 3),
                        CompoundComponent.of(PolyatomicIons.PHOSPHATE)
                ),
                PHOSPHOROUS_ACID = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN, 3),
                        CompoundComponent.of(PolyatomicIons.PHOSPHIDE)
                ),
                CARBONIC_ACID = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN, 2),
                        CompoundComponent.of(PolyatomicIons.CARBONATE)
                ),
                ACETIC_ACID = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN),
                        CompoundComponent.of(PolyatomicIons.ACETATE)
                ),
                PERCHLORIC_ACID = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN),
                        CompoundComponent.of(PolyatomicIons.PERCHLORATE)
                ),
                CHLORIC_ACID = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN),
                        CompoundComponent.of(PolyatomicIons.CHLORATE)
                ),
                CHLOROUS_ACID = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN),
                        CompoundComponent.of(PolyatomicIons.CHLORITE)
                ),
                HYPOCHLOROUS_ACID = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN),
                        CompoundComponent.of(PolyatomicIons.CHLORITE)
                ),
                HYDROCHLORIC_ACID = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN),
                        CompoundComponent.of(Elements.CHLORINE)
                ),
                HYDROBROMIC_ACID = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN),
                        CompoundComponent.of(Elements.BROMINE)
                ),
                HYDROIODIC_ACID = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN),
                        CompoundComponent.of(Elements.IODINE)
                ),
                HYDROFLUORIC_ACID = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN),
                        CompoundComponent.of(Elements.FLUORINE)
                );
    }

    public static class PolyatomicIons {
        public static final Compound
                AMMONIUM = Compound.of(
                        CompoundComponent.of(Elements.NITROGEN),
                        CompoundComponent.of(Elements.HYDROGEN, 4)
                ),
                ACETATE = Compound.of(
                        CompoundComponent.of(Elements.CARBON),
                        CompoundComponent.of(Elements.HYDROGEN, 3),
                        CompoundComponent.of(Elements.CARBON),
                        CompoundComponent.of(Elements.OXYGEN),
                        CompoundComponent.of(Elements.OXYGEN)
                ),
                DIHYDROGEN_PHOSPHATE = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN, 2),
                        CompoundComponent.of(Elements.PHOSPHOROUS),
                        CompoundComponent.of(Elements.OXYGEN, 4)
                ),
                HYDROGEN_SULPHITE = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN),
                        CompoundComponent.of(Elements.SULFUR),
                        CompoundComponent.of(Elements.OXYGEN, 3)
                ),
                HYDROGEN_SULPHATE = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN),
                        CompoundComponent.of(Elements.SULFUR),
                        CompoundComponent.of(Elements.OXYGEN, 4)
                ),
                HYDROGEN_CARBONATE = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN),
                        CompoundComponent.of(Elements.CARBON),
                        CompoundComponent.of(Elements.OXYGEN, 3)
                ),
                PERMANGANATE = Compound.of(
                        CompoundComponent.of(Elements.MANGANESE),
                        CompoundComponent.of(Elements.OXYGEN, 4)
                ),
                NITRIDE = Compound.of(
                        CompoundComponent.of(Elements.NITROGEN),
                        CompoundComponent.of(Elements.OXYGEN, 2)
                ),
                NITRATE = Compound.of(
                        CompoundComponent.of(Elements.NITROGEN),
                        CompoundComponent.of(Elements.OXYGEN, 3)
                ),
                CYANIDE = Compound.of(
                        CompoundComponent.of(Elements.CARBON),
                        CompoundComponent.of(Elements.NITROGEN)
                ),
                HYDROXIDE = Compound.of(
                        CompoundComponent.of(Elements.OXYGEN),
                        CompoundComponent.of(Elements.HYDROGEN)
                ),
                HYPOCHLORITE = Compound.of(
                        CompoundComponent.of(Elements.CHLORINE),
                        CompoundComponent.of(Elements.OXYGEN)
                ),
                CHLORITE = Compound.of(
                        CompoundComponent.of(Elements.CHLORINE),
                        CompoundComponent.of(Elements.OXYGEN, 2)
                ),
                CHLORATE = Compound.of(
                        CompoundComponent.of(Elements.CHLORINE),
                        CompoundComponent.of(Elements.OXYGEN, 3)
                ),
                PERCHLORATE = Compound.of(
                        CompoundComponent.of(Elements.CHLORINE),
                        CompoundComponent.of(Elements.OXYGEN, 4)
                ),
                CARBONATE = Compound.of(
                        CompoundComponent.of(Elements.CARBON),
                        CompoundComponent.of(Elements.OXYGEN, 3)
                ),
                CHROMATE = Compound.of(
                        CompoundComponent.of(Elements.CHROMIUM),
                        CompoundComponent.of(Elements.OXYGEN, 4)
                ),
                DICHROMATE = Compound.of(
                        CompoundComponent.of(Elements.CHROMIUM, 2),
                        CompoundComponent.of(Elements.OXYGEN, 7)
                ),
                HYDROGEN_PHOSPHATE = Compound.of(
                        CompoundComponent.of(Elements.HYDROGEN, 1),
                        CompoundComponent.of(Elements.PHOSPHOROUS),
                        CompoundComponent.of(Elements.OXYGEN, 4)
                ),
                OXALATE = Compound.of(
                        CompoundComponent.of(Elements.CARBON, 2),
                        CompoundComponent.of(Elements.OXYGEN, 4)
                ),
                SILICATE = Compound.of(
                        CompoundComponent.of(Elements.SILICON),
                        CompoundComponent.of(Elements.OXYGEN, 3)
                ),
                SULFITE = Compound.of(
                        CompoundComponent.of(Elements.SULFUR),
                        CompoundComponent.of(Elements.OXYGEN, 3)
                ),
                SULFATE = Compound.of(
                        CompoundComponent.of(Elements.SULFUR),
                        CompoundComponent.of(Elements.OXYGEN, 4)
                ),
                PHOSPHIDE = Compound.of(
                        CompoundComponent.of(Elements.PHOSPHOROUS),
                        CompoundComponent.of(Elements.OXYGEN, 3)
                ),
                PHOSPHATE = Compound.of(
                        CompoundComponent.of(Elements.PHOSPHOROUS),
                        CompoundComponent.of(Elements.OXYGEN, 4)
                );
    }
}
