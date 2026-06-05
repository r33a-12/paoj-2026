package com.pao.laboratory14.exercise1;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

class Bilet {
    int id;
    String eveniment;
    TipBilet tip;
    double pret;

    public Bilet(int id, String eveniment, TipBilet tip, double pret) {
        this.id = id;
        this.eveniment = eveniment;
        this.tip = tip;
        this.pret = pret;
    }
}

class RaportVanzari {
    final Map<TipBilet, Long> numarPerTip;
    final Map<TipBilet, Double> incasariPerTip;
    final double totalGlobal;
    final double medieGlobala;
    final TipBilet tipCelMaiPopular;

    public RaportVanzari(Map<TipBilet, Long> numarPerTip, Map<TipBilet, Double> incasariPerTip,
                         double totalGlobal, double medieGlobala, TipBilet tipCelMaiPopular) {
        this.numarPerTip = Collections.unmodifiableMap(new HashMap<>(numarPerTip));
        this.incasariPerTip = Collections.unmodifiableMap(new HashMap<>(incasariPerTip));
        this.totalGlobal = totalGlobal;
        this.medieGlobala = medieGlobala;
        this.tipCelMaiPopular = tipCelMaiPopular;
    }
}

class RaportCollector implements Collector<Bilet, Map<TipBilet, double[]>, RaportVanzari> {
    @Override
    public Supplier<Map<TipBilet, double[]>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<TipBilet, double[]>, Bilet> accumulator() {
        return (map, bilet) -> {
            double[] stats = map.computeIfAbsent(bilet.tip, _ -> new double[2]);
            stats[0] += 1;
            stats[1] += bilet.pret;
        };
    }

    @Override
    public BinaryOperator<Map<TipBilet, double[]>> combiner() {
        return (map1, map2) -> {
            map2.forEach((k, v) -> {
                double[] stats1 = map1.computeIfAbsent(k, _ -> new double[2]);
                stats1[0] += v[0];
                stats1[1] += v[1];
            });
            return map1;
        };
    }

    @Override
    public Function<Map<TipBilet, double[]>, RaportVanzari> finisher() {
        return map -> {
            Map<TipBilet, Long> numarPerTip = new HashMap<>();
            Map<TipBilet, Double> incasariPerTip = new HashMap<>();
            double totalGlobal = 0;
            long totalCount = 0;

            TipBilet popular = null;
            long maxCount = -1;

            for (TipBilet tip : TipBilet.values()) {
                if (map.containsKey(tip)) {
                    double[] stats = map.get(tip);
                    long count = (long) stats[0];
                    double incasari = stats[1];
                    numarPerTip.put(tip, count);
                    incasariPerTip.put(tip, incasari);
                    totalGlobal += incasari;
                    totalCount += count;

                    if (count > maxCount) {
                        maxCount = count;
                        popular = tip;
                    }
                }
            }

            double medieGlobala = totalCount > 0 ? totalGlobal / totalCount : 0;
            return new RaportVanzari(numarPerTip, incasariPerTip, totalGlobal, medieGlobala, popular);
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();

        List<Bilet> bilete = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            String ev = scanner.next();
            TipBilet tip = TipBilet.valueOf(scanner.next());
            double pret = scanner.nextDouble();
            bilete.add(new Bilet(id, ev, tip, pret));
        }

        String comanda = scanner.next();

        RaportVanzari raport = bilete.stream().collect(new RaportCollector());

        Arrays.stream(TipBilet.values())
                .filter(raport.numarPerTip::containsKey)
                .forEach(tip -> System.out.printf(Locale.US, "%s: count=%d incasari=%.2f RON\n",
                        tip, raport.numarPerTip.get(tip), raport.incasariPerTip.get(tip)));

        if ("RAPORT_COMPLET".equals(comanda)) {
            System.out.println("---");
            System.out.printf(Locale.US, "Total: %.2f RON\n", raport.totalGlobal);
            System.out.printf(Locale.US, "Medie: %.2f RON\n", raport.medieGlobala);
            System.out.println("Cel mai popular: " + (raport.tipCelMaiPopular != null ? raport.tipCelMaiPopular : "NONE"));
        }
    }
}
